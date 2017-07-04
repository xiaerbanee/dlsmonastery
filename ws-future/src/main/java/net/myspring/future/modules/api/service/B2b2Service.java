package net.myspring.future.modules.api.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.api.domain.CarrierOrder;
import net.myspring.future.modules.api.domain.CarrierProduct;
import net.myspring.future.modules.api.dto.B2bOrderDetailDto;
import net.myspring.future.modules.api.dto.B2bOrderDto;
import net.myspring.future.modules.api.dto.GoodsOrderB2bDto;
import net.myspring.future.modules.api.repository.CarrierOrderRepository;
import net.myspring.future.modules.api.repository.CarrierProductRepository;
import net.myspring.future.modules.api.web.form.B2b2Form;
import net.myspring.future.modules.api.web.query.B2b2Query;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.domain.GoodsOrderIme;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderImeRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderRepository;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.MD5Utils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class B2b2Service {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, OkHttpClient> okHttpClientMap = Maps.newHashMap();

    private static Map<String, Object> commonYdMap = Maps.newHashMap();

    static {
        commonYdMap.put("oppoStatus", false);
        commonYdMap.put("vivoStatus", false);
        commonYdMap.put("oppoClickStatus", false);
        commonYdMap.put("vivoClickStatus", false);
        commonYdMap.put("cdOppoStatus", false);
        commonYdMap.put("cdVivoStatus", false);
        commonYdMap.put("oppoTuiStatus", false);
        commonYdMap.put("vivoTuiStatus", false);
    }

    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private CarrierOrderRepository carrierOrderRepository;
    @Autowired
    private GoodsOrderImeRepository goodsOrderImeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CarrierProductRepository carrierProductRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    public Response getYzmPic() {
        try {
            OkHttpClient client = getClient("loginBefore");
            String url = "http://jx.10086.cn/jxb2b2/login/login";
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            client.cookieJar().saveFromResponse(HttpUrl.parse(url), Cookie.parseAll(HttpUrl.parse(url), response.headers()));
            response.close();
            url = "http://jx.10086.cn/jxb2b2/common/yzm1.jsp";
            request = new Request.Builder().url(url).build();
            response = client.newCall(request).execute();
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public boolean login(B2b2Form b2b2Form) {
        try {
            String username= B2b2Form.OppoUsernameEnum.ncop.name();
            String statusKey = "oppoStatus";
            if (b2b2Form.isVivo()) {
                statusKey = "vivoStatus";
                username= B2b2Form.VivoUsernameEnum.ksdz.name();
            }
            if (!(boolean) commonYdMap.get(statusKey)) {
                OkHttpClient client = getClient("loginBefore");
                String url = "http://jx.10086.cn/jxb2b2/login/loginMain";
                String password = Base64Utils.encodeToString(MD5Utils.encode(b2b2Form.getPassword()).getBytes());
                RequestBody body = new FormBody.Builder()
                        .add("username", b2b2Form.getUsername())
                        .add("password", password)
                        .add("password_mj", password)
                        .add("yzm", b2b2Form.getYzm())
                        .add("yzmTime", new Date().toString())
                        .add("userType", "0")
                        .build();
                Request request = new Request.Builder().post(body).url(url).build();
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                if (result.contains("1")) {
                    client.cookieJar().saveFromResponse(HttpUrl.parse(url), Cookie.parseAll(HttpUrl.parse(url), response.headers()));
                    commonYdMap.put("currentUsername", username);
                    okHttpClientMap.put(username, client);
                    logger.info("登陆成功");
                    response.close();
                    return true;
                } else {
                    response.close();
                    logger.info("登陆失败,重新登陆");
                    return false;
                }
            } else {
                commonYdMap.put("currentUsername", username);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //设置任务执行状态
    public Map<String, Object> setStatus(B2b2Form b2b2Form) {
        String statusKey = "oppoStatus";
        String clickStatusKey = "oppoClickStatus";
        if (b2b2Form.isVivo()) {
            statusKey = "vivoStatus";
            clickStatusKey = "vivoClickStatus";
        }
        if ((boolean) commonYdMap.get(clickStatusKey)) {
            commonYdMap.put(clickStatusKey, false);
        } else {
            commonYdMap.put(clickStatusKey, true);
        }
        if ((boolean) commonYdMap.get(clickStatusKey) && !(boolean) commonYdMap.get(statusKey)) {
            getTask(b2b2Form);
        }
        return commonYdMap;
    }

    //推送任务
    public List<String> tuiTask(B2b2Form b2b2Form) {
        List<String> failCodeList = Lists.newArrayList();
        String tuiStatusKey = "oppoTuiStatus";
        String tuiTotalQtyKey = "oppoTuiTotalQty";
        String tuiQtyKey = "oppoTuiQty";
        String failCodeKey = "oppoFailCode";
        if (b2b2Form.isVivo()) {
            tuiStatusKey = "vivoTuiStatus";
            tuiTotalQtyKey = "vivoTuiTotalQty";
            tuiQtyKey = "vivoTuiQty";
            failCodeKey = "vivoFailCode";
        }
        commonYdMap.put(failCodeKey, Lists.newArrayList());
        commonYdMap.put(tuiQtyKey, 0);
        commonYdMap.put(tuiTotalQtyKey, b2b2Form.getCodeList().size());
        OkHttpClient client = getClient(b2b2Form.getUsername());
        if (!(boolean) commonYdMap.get(tuiStatusKey)) {
            List<CarrierOrder> carrierOrders = carrierOrderRepository.findByCodeIn(b2b2Form.getCodeList());
            List<GoodsOrder> goodsOrders = goodsOrderRepository.findByIdInAndEnabledIsTrue(CollectionUtil.extractToList(carrierOrders, "goodsOrderId"));
            List<CarrierOrder> carrierOrderList = carrierOrderRepository.findByGoodsOrderIdIn(CollectionUtil.extractToList(goodsOrders, "id"));
            List<GoodsOrderDetail> goodsOrderDetails = goodsOrderDetailRepository.findByGoodsOrderIdIn(CollectionUtil.extractToList(goodsOrders, "id"));
            List<CarrierProduct> carrierProductList = carrierProductRepository.findByProductIdIn(CollectionUtil.extractToList(goodsOrderDetails, "productId"));

            Map<String,List<GoodsOrderDetail>> goodsOrderDetailMap= CollectionUtil.extractToMap(goodsOrderDetails,"goodsOrderId");
            Map<String,List<CarrierOrder>> carrierOrderMap= CollectionUtil.extractToMapList(carrierOrderList,"goodsOrderId");
            List<GoodsOrderB2bDto> goodsOrderB2bDtoList= BeanUtil.map(goodsOrders,GoodsOrderB2bDto.class);
            for(GoodsOrderB2bDto goodsOrderB2bDto:goodsOrderB2bDtoList){
                goodsOrderB2bDto.setCarrierOrderList(carrierOrderMap.get(goodsOrderB2bDto.getId()));
                goodsOrderB2bDto.setGoodsOrderDetailList(goodsOrderDetailMap.get(goodsOrderB2bDto.getId()));
            }

            Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderDetails, "productId"));
            Map<String, CarrierProduct> carrierProductMap = CollectionUtil.extractToMap(carrierProductList, "name");

            for (GoodsOrderB2bDto goodsOrder : goodsOrderB2bDtoList) {
                try {
                    List<B2bOrderDto> b2bOrders = Lists.newArrayList();
                    for (CarrierOrder carrierOrder : goodsOrder.getCarrierOrderList()) {
                        commonYdMap.put(tuiQtyKey, (Integer) commonYdMap.get(tuiQtyKey) + 1);
                        B2bOrderDto b2bOrder = getTuiB2bOrder(client, carrierOrder.getCode());
                        if (!b2bOrder.getStatus()) {
                            break;
                        }
                        Map<String, String> detailMap = ObjectMapperUtils.readValue(carrierOrder.getDetailJson(), Map.class);
                        String mallOrderId = detailMap.get("mallOrderId");
                        b2bOrder.setMallOrderCode(carrierOrder.getCode());
                        b2bOrder.setMallOrderId(mallOrderId);
                        b2bOrders.add(b2bOrder);
                    }
                    for (B2bOrderDto b2bOrder : b2bOrders) {
                        for (B2bOrderDetailDto b2bOrderDetail : b2bOrder.getB2bOrderDetailList()) {
                            String mallProductTypeAndColor = b2bOrderDetail.getMallProductTypeAndColor();
                            String key = carrierProductMap.get(mallProductTypeAndColor).getProductId();
                            if (!goodsOrder.getExtra().containsKey(key)) {
                                goodsOrder.getExtra().put(key, 0);
                            }
                            goodsOrder.getExtra().put(key, (Integer) goodsOrder.getExtra().get(key) + b2bOrderDetail.getQty());
                        }
                    }
                    Map<String, Integer> mallProductQtyMap = getProductQtyMap(goodsOrder, productMap);
                    boolean isSure = true;
                    if (mallProductQtyMap.size() != goodsOrder.getExtra().size()) {
                        isSure = false;
                        failCodeList.addAll(CollectionUtil.extractToList(b2bOrders, "mallOrderCode"));
                    } else {
                        for (Map.Entry<String, Integer> entry : mallProductQtyMap.entrySet()) {
                            if (goodsOrder.getExtra().get(entry.getKey()) != entry.getValue()) {
                                failCodeList.addAll(CollectionUtil.extractToList(goodsOrder.getCarrierOrderList(), "code"));
                                isSure = false;
                            }
                        }
                    }
                    if (isSure) {
                        for (B2bOrderDto b2bOrder : b2bOrders) {
                            logger.info(b2bOrder.getMallOrderCode() + "推送开始");
                            if (b2b2Form.getCodeList().contains(b2bOrder.getMallOrderCode())) {
                                String msg = tsKjOrder(client, b2bOrder.getMallOrderId());
                                if (!"0".equals(msg)) {
                                    failCodeList.add(b2bOrder.getMallOrderCode());
                                    commonYdMap.put(failCodeKey, failCodeList);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failCodeList.addAll(CollectionUtil.extractToList(goodsOrder.getCarrierOrderList(), "code"));
                    commonYdMap.put(failCodeKey, failCodeList);
                    logger.error(e.getMessage());
                }
            }
        }
        commonYdMap.put(tuiTotalQtyKey, 0);
        logger.info("推送完成,失败个数：" + failCodeList.size());
        return failCodeList;
    }

    //推送昌东数据
    public void startCdTask(B2b2Form b2b2Form) {
        OkHttpClient client = getClient(b2b2Form.getUsername());
        String cdStatusKey = "cdOppoStatus";
        if (b2b2Form.isVivo()) {
            cdStatusKey = "cdVivoStatus";
        }
        if (!(boolean) commonYdMap.get(cdStatusKey)) {
            List<String> businessIds = Lists.newArrayList();
            for (String orderId : b2b2Form.getOrderIdList()) {
                businessIds.add(orderId.replace("XK", "").trim());
            }
            String totalQtyKey = "oppoCdTotalQty";
            String qtyKey = "oppoCdQty";
            String failOrderIdsKey = "oppoCdFailOrderIds";
            if (b2b2Form.isVivo()) {
                totalQtyKey = "vivoCdTotalQty";
                qtyKey = "vivoCdQty";
                failOrderIdsKey = "vivoCdFailOrderIds";
            }
            commonYdMap.put(totalQtyKey, b2b2Form.getOrderIdList().size());
            commonYdMap.put(qtyKey, 0L);
            List<GoodsOrder> goodsOrderList = goodsOrderRepository.findByBusinessIdIn(businessIds);
            List<GoodsOrderB2bDto> goodsOrderB2bDtoList= BeanUtil.map(goodsOrderList,GoodsOrderB2bDto.class);
            List<String> failGoodsOrders = startTask(client, goodsOrderB2bDtoList, qtyKey);
            Map<String,GoodsOrder> goodsOrderMap= CollectionUtil.extractToMap(goodsOrderList,"businessId");
            for(String businessId:failGoodsOrders){
                if(goodsOrderMap.containsKey(businessId)){
                    goodsOrderList.remove(goodsOrderMap.get(businessId));
                }
            }
            if(CollectionUtil.isNotEmpty(goodsOrderList)){
                goodsOrderRepository.updateStatusByIdIn( "已推送", CollectionUtil.extractToList(goodsOrderList, "id"));
            }
            commonYdMap.put(totalQtyKey, 0L);
            commonYdMap.put(qtyKey, 0L);
            commonYdMap.put(failOrderIdsKey, failGoodsOrders);
        }
    }

    public boolean checkLogin(String username) {
        try {
            OkHttpClient client = getClient(username);
            String url = "http://jx.10086.cn/jxb2b2/order/querySellerOrderList";
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            String html = response.body().string();
            Document document = Jsoup.parse(html);
            if (document.getElementsByClass("login_form").size() != 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Map<String, Integer> getProductQtyMap(GoodsOrderB2bDto goodsOrder, Map<String, Product> productMap) {
        Map<String, Integer> productQtyMap = Maps.newHashMap();
        for (GoodsOrderDetail goodsOrderDetail : goodsOrder.getGoodsOrderDetailList()) {
            String productName = productMap.get(goodsOrderDetail.getProductId()).getName();
            String key = goodsOrderDetail.getProductId();
            if (!"代收运费".equals(productName)) {
                if (!productQtyMap.containsKey(key)) {
                    productQtyMap.put(key, 0);
                }
                productQtyMap.put(key, productQtyMap.get(key) + goodsOrderDetail.getBillQty());
            }
        }
        return productQtyMap;
    }

    private void getTask(B2b2Form b2b2Form) {
        String statusKey = "oppoStatus";
        if (b2b2Form.isVivo()) {
            statusKey = "vivoStatus";
        }
        OkHttpClient okHttpClient = getClient(b2b2Form.getUsername());
        carrierOrderRepository.setStatusByLocked("验证中", true);
        int page = 0;
        int size =25;
        Page<GoodsOrder> goodsOrderPage = null;
        Map<String, Object> paramMap = Maps.newHashMap();
        String qtyKey = "oppoQty";
        String totalQtyKey = "oppoTotalQty";
        String clickStatusKey = "oppoClickStatus";
        List<String> proxyAreaIdList=Lists.newArrayList( "599");
        String storeId= CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.DEFALULT_CARRIAR_STORE_ID.name()).getValue();
        if (b2b2Form.isVivo()) {
            qtyKey = "vivoQty";
            clickStatusKey = "vivoClickStatus";
            totalQtyKey = "vivoTotalQty";
            proxyAreaIdList=Lists.newArrayList( "1069");
        }
        commonYdMap.put(statusKey, true);
        B2b2Query b2b2Query= BeanUtil.map(b2b2Form,B2b2Query.class);
        b2b2Query.setProxyAreaIdList(proxyAreaIdList);
        b2b2Query.setStoreId(storeId);
        b2b2Query.setStatus("空值");
        while ((page == 0 || goodsOrderPage.getContent().size() != 0) && (boolean) commonYdMap.get(clickStatusKey)) {
            Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
            goodsOrderPage = goodsOrderRepository.findB2bTask(pageable, b2b2Query);
            commonYdMap.put(qtyKey, Long.valueOf(20 * page));
            commonYdMap.put(totalQtyKey, goodsOrderPage.getTotalElements());
            if (CollectionUtil.isNotEmpty(goodsOrderPage.getContent())) {
                List<GoodsOrderB2bDto> goodsOrderB2bDtoList= BeanUtil.map(goodsOrderPage.getContent(),GoodsOrderB2bDto.class);
                startTask(okHttpClient, goodsOrderB2bDtoList, qtyKey);
            }
            page++;
        }
        carrierOrderRepository.setStatusByLocked("验证中", true);
        carrierOrderRepository.setLocked(false);
        getValidatorTask(okHttpClient, b2b2Query);
        commonYdMap.put(statusKey, false);
        commonYdMap.put(clickStatusKey, false);
        commonYdMap.put(totalQtyKey, 0L);
        commonYdMap.put(qtyKey, 0L);
    }

    //开始执行发货任务
    private List<String> startTask(OkHttpClient client, List<GoodsOrderB2bDto> goodsOrderB2bDtoList, String qtyKey) {
        List<GoodsOrderB2bDto> postGoodsOrders = Lists.newArrayList();
        List<String> failGoodsOrders = Lists.newArrayList();
        Map<String, Map<String, List<String>>> map = getImeMap(goodsOrderB2bDtoList);
        for (GoodsOrderB2bDto goodsOrder : goodsOrderB2bDtoList) {
            try {
                commonYdMap.put(qtyKey, (Long) commonYdMap.get(qtyKey) + 1);
                List<String> useImeList = Lists.newArrayList();
                List<CarrierOrder> carrierOrders = goodsOrder.getCarrierOrderList();
                logger.info(goodsOrder.getBusinessId() + "OA串码获取开始");
                Map<String, List<String>> imeMap = map.get(goodsOrder.getId());
                for (CarrierOrder carrierOrder : carrierOrders) {
                    logger.info(carrierOrder.getCode() + "开始任务");
                    String url = "http://jx.10086.cn/jxb2b2/order/querySellerOrderList?&orderNo=" + carrierOrder.getCode();
                    String orderQueryHtml = getOrderQueryHtml(client, url);
                    if (StringUtils.isEmpty(orderQueryHtml)) {
                        failGoodsOrders.add(goodsOrder.getBusinessId());
                        continue;
                    }
                    String mallOrderId = getChOrderId(client, orderQueryHtml, carrierOrder.getCode());
                    if (StringUtils.isEmpty(mallOrderId)) {
                        failGoodsOrders.add(goodsOrder.getBusinessId());
                        continue;
                    }
                    String sendOrderHtml = getSendOrderHtml(client, mallOrderId);
                    if (StringUtils.isEmpty(sendOrderHtml)) {
                        failGoodsOrders.add(goodsOrder.getBusinessId());
                        continue;
                    }
                    List<String> selectOptions = getSelectOptions(sendOrderHtml);
                    if (CollectionUtil.isEmpty(selectOptions)) {
                        failGoodsOrders.add(goodsOrder.getBusinessId());
                        continue;
                    }
                    B2bOrderDto b2bOrder = new B2bOrderDto();
                    b2bOrder.setMallOrderCode(carrierOrder.getCode());
                    b2bOrder.setDeliveryNo("邮政");
                    b2bOrder.setDeliveryCompany("1");
                    b2bOrder.setMallOrderId(mallOrderId);
                    for (String optionValue : selectOptions) {
                        B2bOrderDetailDto b2bOrderDetail = new B2bOrderDetailDto();
                        String[] values = optionValue.split(CharConstant.COMMA);
                        b2bOrderDetail.setMallProductId(values[3]);
                        b2bOrderDetail.setMallProductType(values[1]);
                        Integer qty = getQty(client, mallOrderId, values[3]);
                        b2bOrderDetail.setQty(qty);
                        logger.info(imeMap.toString());
                        logger.info(values[1]);
                        logger.info(qty.toString());
                        List<String> mallProductIme = new ArrayList<>(imeMap.get(values[1]));
                        List<String> imes = getSendIme(useImeList, mallProductIme, qty);
                        if (CollectionUtil.isEmpty(imes)) {
                            logger.info(goodsOrder.getBusinessId()+ "串码数量少于发货数量");
                            break;
                        }
                        b2bOrderDetail.setImes(imes);
                        imeMap.get(values[1]).removeAll(imes);
                        b2bOrder.getB2bOrderDetailList().add(b2bOrderDetail);
                    }
                    if (CollectionUtil.isNotEmpty(b2bOrder.getB2bOrderDetailList()) && b2bOrder.getB2bOrderDetailList().size() == selectOptions.size()) {
                        goodsOrder.getB2bOrderList().add(b2bOrder);
                    } else {
                        failGoodsOrders.add(goodsOrder.getBusinessId());
                        if (CollectionUtil.isNotEmpty(b2bOrder.getB2bOrderDetailList())) {
                            for (B2bOrderDetailDto b2bOrderDetail : b2bOrder.getB2bOrderDetailList()) {
                                imeMap.get(b2bOrderDetail.getMallProductType()).addAll(b2bOrderDetail.getImes());
                            }
                        }
                    }
                }
                if(CollectionUtil.isNotEmpty(goodsOrder.getB2bOrderList())){
                    postGoodsOrders.add(goodsOrder);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                failGoodsOrders.add(goodsOrder.getBusinessId());
            }
        }
        logger.info("推送数据开始,推送个数" + postGoodsOrders.size());
        List<String> postFailGoodsOrders = postGoodsOrderToB2b(client, postGoodsOrders);
        failGoodsOrders.addAll(CollectionUtil.extractToList(postFailGoodsOrders,"businessId"));
        logger.info("推送数据失败个数" + failGoodsOrders.size());
        return failGoodsOrders;
    }

    private List<String> postGoodsOrderToB2b(OkHttpClient client, List<GoodsOrderB2bDto> goodsOrders) {
        List<String> failGoodsOrder = Lists.newArrayList();
        for (GoodsOrderB2bDto postGoodsOrder : goodsOrders) {
            try {
                logger.info(postGoodsOrder.getBusinessId() + "推送开始");
                boolean goodsOrderSuccess = true;
                for (B2bOrderDto b2bOrder  :postGoodsOrder.getB2bOrderList()) {
                    boolean carrierSuccess = true;
                    List<String> useImes = Lists.newArrayList();
                    for (B2bOrderDetailDto b2bOrderDetail : b2bOrder.getB2bOrderDetailList()) {
                        useImes.addAll(b2bOrderDetail.getImes());
                        logger.info(b2bOrder.getMallOrderCode() + "importOneMobileImei开始");
                        for (String ime : b2bOrderDetail.getImes()) {
                            String msg = importOneMobileImei(client, ime, b2bOrder.getMallOrderId(), b2bOrderDetail.getMallProductId());
                            if (!"0".equals(msg) && !"2".equals(msg)) {
                                carrierSuccess = false;
                                goodsOrderSuccess = false;
                            }
                        }
                    }
                    if (carrierSuccess) {
                        String msg = checkOrder(client, b2bOrder.getMallOrderId());
                        if ("0".equals(msg)) {
                            logger.info(b2bOrder.getMallOrderCode() + "sendHuo获取开始");
                            sendHuo(client, b2bOrder.getMallOrderId(), b2bOrder.getB2bOrderDetailList(), b2bOrder.getDeliveryNo(), b2bOrder.getDeliveryCompany());
                            carrierOrderRepository.setLockedByCode( true,b2bOrder.getMallOrderCode());
                            carrierOrderRepository.updateImesByCode(StringUtils.join(useImes, CharConstant.COMMA), b2bOrder.getMallOrderCode());
                        } else {
                            goodsOrderSuccess = false;
                        }
                    }
                }
                if (!goodsOrderSuccess) {
                    failGoodsOrder.add(postGoodsOrder.getBusinessId());
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return failGoodsOrder;
    }

    //获取发货串码
    private List<String> getSendIme(List<String> useImeList, List<String> imeList, Integer qty) {
        List<String> sendImeList = Lists.newArrayList();
        if (qty <= imeList.size()) {
            for (int i = imeList.size() - 1; i >= 0; i--) {
                String ime = imeList.get(i);
                if (!useImeList.contains(ime)) {
                    sendImeList.add(ime);
                    useImeList.add(ime);
                    if (sendImeList.size() == qty) {
                        break;
                    }
                }
            }
        }
        return sendImeList;
    }

    //在oa系统中获取匹配的发货串码
    private Map<String, Map<String, List<String>>> getImeMap(List<GoodsOrderB2bDto> goodsOrderB2bDtoList) {
        Map<String, Map<String, List<String>>> imeMap = Maps.newHashMap();

        List<String> goodsOrderIds = CollectionUtil.extractToList(goodsOrderB2bDtoList, "id");
        List<CarrierOrder> carrierOrderList = carrierOrderRepository.findByGoodsOrderIdsAndImesIsNull(goodsOrderIds);
        List<GoodsOrderIme> goodsOrderImeList = goodsOrderImeRepository.findByGoodsOrderIdIn(goodsOrderIds);
        List<CarrierProduct> carrierProductList = carrierProductRepository.findByProductIdIn(CollectionUtil.extractToList(goodsOrderImeList, "productId"));

        Map<String, List<CarrierProduct>> carrierProductMap = CollectionUtil.extractToMapList(carrierProductList, "productId");
        Map<String, ProductIme> productImeMap = productImeRepository.findMap(CollectionUtil.extractToList(goodsOrderImeList, "productImeId"));

        Map<String,List<CarrierOrder>> carrierOrderMap= CollectionUtil.extractToMapList(carrierOrderList,"goodsOrderId");
        Map<String,List<GoodsOrderIme>> goodsOrderImeMap= CollectionUtil.extractToMapList(goodsOrderImeList,"goodsOrderId");
        for(GoodsOrderB2bDto goodsOrderB2bDto:goodsOrderB2bDtoList){
            goodsOrderB2bDto.setCarrierOrderList(carrierOrderMap.get(goodsOrderB2bDto.getId()));
            goodsOrderB2bDto.setGoodsOrderImeList(goodsOrderImeMap.get(goodsOrderB2bDto.getId()));
        }

        Map<String, List<String>> useImeMap = Maps.newHashMap();
        List<CarrierOrder> carrierOrders = carrierOrderRepository.findByGoodsOrderIdsAndImesNotNull(goodsOrderIds);
        for (CarrierOrder carrierOrder : carrierOrders) {
            String key = carrierOrder.getGoodsOrderId();
            if (!useImeMap.containsKey(key)) {
                useImeMap.put(key, Lists.newArrayList());
            }
            useImeMap.get(key).addAll(StringUtils.getSplitList(carrierOrder.getImes(), CharConstant.COMMA));
        }
        for (GoodsOrderB2bDto goodsOrder : goodsOrderB2bDtoList) {
            Map<String, List<String>> map = Maps.newHashMap();
            List<String> imes = useImeMap.get(goodsOrder.getId());
            for (GoodsOrderIme goodsOrderIme : goodsOrder.getGoodsOrderImeList()) {
                ProductIme productIme = productImeMap.get(goodsOrderIme.getProductImeId());
                if (productIme != null) {
                    String value = productIme.getIme();
                    if (CollectionUtil.isEmpty(imes) || !imes.contains(value)) {
                        List<CarrierProduct> carrierProducts = carrierProductMap.get(goodsOrderIme.getProductId());
                        if (CollectionUtil.isNotEmpty(carrierProducts)) {
                            for (CarrierProduct carrierProduct : carrierProducts) {
                                String key = carrierProduct.getMallProductTypeName();
                                if (!map.containsKey(key)) {
                                    map.put(key, Lists.newArrayList());
                                }
                                map.get(key).add(value);
                            }
                        } else {
                            logger.info(goodsOrderIme.getProductId() + "  该产品ID未设置商城商品");
                        }
                    }
                } else {
                    logger.info(goodsOrderIme.getProductId() + "  该产品ID未设置商城商品");
                }
            }
            imeMap.put(goodsOrder.getId(), map);
        }
        logger.info("OA串码获取结束");
        return imeMap;
    }

    private void getValidatorTask(OkHttpClient client, B2b2Query b2b2Query) {
        int page = 0;
        Page<GoodsOrder> goodsOrderPage = null;
        while ((page == 0 || goodsOrderPage.getContent().size() != 0)) {
            Pageable pageable = new PageRequest(page, 20, Sort.Direction.ASC, "id");
            goodsOrderPage = goodsOrderRepository.findB2bTask(pageable, b2b2Query);
            if (CollectionUtil.isNotEmpty(goodsOrderPage.getContent())) {
                List<GoodsOrderB2bDto> goodsOrderB2bDtoList= BeanUtil.map(goodsOrderPage.getContent(),GoodsOrderB2bDto.class);
                startValidatorTask(client, goodsOrderB2bDtoList);
            }
            page++;
        }
        carrierOrderRepository.setStatusByLocked("已导入", true);
        carrierOrderRepository.setLocked(false);
    }

    private void startValidatorTask(OkHttpClient client, List<GoodsOrderB2bDto> goodsOrderB2bDtoList) {
        List<CarrierOrder> carrierOrders = carrierOrderRepository.findByGoodsOrderIdIn(CollectionUtil.extractToList(goodsOrderB2bDtoList, "id"));
        Map<String,List<CarrierOrder>> carrierOrderMap= CollectionUtil.extractToMapList(carrierOrders,"goodsOrderId");
        for(GoodsOrderB2bDto goodsOrderB2bDto:goodsOrderB2bDtoList){
            goodsOrderB2bDto.setCarrierOrderList(carrierOrderMap.get(goodsOrderB2bDto.getId()));
        }
        for (GoodsOrderB2bDto goodsOrder : goodsOrderB2bDtoList) {
            for (CarrierOrder carrierOrder : goodsOrder.getCarrierOrderList()) {
                try {
                    String url = "http://jx.10086.cn/jxb2b2/order/querySellerOrderList?&orderNo=" + carrierOrder.getCode();
                    String orderQueryHtml = getOrderQueryHtml(client, url);
                    Document document = Jsoup.parse(orderQueryHtml);
                    Element element = document.getElementsByClass("b_newTable").get(0);
                    Elements trs = element.getElementsByTag("tr");
                    if (trs.size() <= 1) {
                        url = "http://jx.10086.cn/jxb2b2/order/querySellerOrderList?orderState=4&isDealIng=y&orderNo=" + carrierOrder.getCode();
                        orderQueryHtml = getOrderQueryHtml(client, url);
                        document = Jsoup.parse(orderQueryHtml);
                        element = document.getElementsByClass("b_newTable").get(0);
                        trs = element.getElementsByTag("tr");
                    }
                    Element parent_mallOrder_span = document.getElementById("sp");
                    String mallOrderId = parent_mallOrder_span.text().replace(",", "");
                    if (carrierOrder.getCode().startsWith("DDC") || carrierOrder.getCode().startsWith("SDDC")) {
                        String ddcPage = getDDCPage(client, mallOrderId);
                        Document ddcDocument = Jsoup.parse(ddcPage);
                        Elements ddcTrs = ddcDocument.getElementsByClass("b_newTable").get(0).getElementsByTag("tr");
                        boolean startSpider = false;
                        for (Element tr : ddcTrs) {
                            if (!tr.hasClass("myorderbox")) {
                                if (tr.getElementsByTag("td").get(0).getElementsByTag("span").get(0).getElementsByTag("a").get(0).text().contains(carrierOrder.getCode())) {
                                    startSpider = true;
                                } else {
                                    startSpider = false;
                                }
                            } else if (startSpider) {
                                if (tr.text().contains("已发货") || tr.text().contains("已收货")) {
                                    carrierOrderRepository.setLockedByCode( true,carrierOrder.getCode());
                                }
                                startSpider = false;
                            }
                        }
                    } else if (carrierOrder.getCode().startsWith("DD") || carrierOrder.getCode().startsWith("SDD")) {
                        Element tr = trs.get(2);
                        if (tr.text().contains("已发货") || tr.text().contains("已收货")) {
                            carrierOrderRepository.setLockedByCode(true,carrierOrder.getCode());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getOrderQueryHtml(OkHttpClient client, String url) throws IOException {
        String html = "";
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        html = response.body().string();
        logger.info("OrderQueryHtml获取结束");
        response.close();
        return html;
    }

    private String getChOrderId(OkHttpClient client, String orderQueryHtml, String code) throws Exception {
        String chOrderId = "";
        Document document = Jsoup.parse(orderQueryHtml);
        Element span_chOrder = document.getElementById("sp");
        if (span_chOrder == null) {
            return chOrderId;
        }
        chOrderId = span_chOrder.text().substring(1, span_chOrder.text().length());
        if (code.contains("DDC")) {
            String html = getDDCPage(client, chOrderId);
            Document cdocument = Jsoup.parse(html);
            Elements myordertitles = cdocument.getElementsByClass("myordertitle");
            for (Element myordertitle : myordertitles) {
                Elements aTags = myordertitle.getElementsByTag("a");
                if (CollectionUtil.isNotEmpty(aTags) && aTags.get(0).text().equals(code)) {
                    String href = aTags.get(0).attr("href");
                    Integer start = href.indexOf("orderId=") + 8;
                    Integer end = href.indexOf("&", start) == -1 ? href.length() : href.indexOf("&");
                    chOrderId = href.substring(start, end);
                    return chOrderId;
                }
            }
        }
        logger.info("ChOrderId获取结束");
        return chOrderId;
    }

    private String getDDCPage(OkHttpClient client, String chOrderId) throws Exception {
        String url = "http://jx.10086.cn/jxb2b2/order/queryAjaxOrderList";
        RequestBody body = new FormBody.Builder()
                .add("orderId", chOrderId)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String html = response.body().string();
        response.close();
        return html;
    }

    private String getSendOrderHtml(OkHttpClient client, String chOrderId) throws IOException {
        String sendOrderHtml = "";
        String url = "http://jx.10086.cn/jxb2b2/order/sendOrdProduct?orderId=" + chOrderId;
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        sendOrderHtml = response.body().string();
        logger.info("SendOrderHtml获取结束");
        response.close();
        return sendOrderHtml;
    }

    private List<String> getSelectOptions(String sendOrderHtml) {
        List<String> getProductIds = Lists.newArrayList();
        Document document = Jsoup.parse(sendOrderHtml);
        Element select = document.getElementById("sjxh");
        if (select == null) {
            return getProductIds;
        }
        List<Node> nodes = select.childNodes();
        for (Node node : nodes) {
            String productName = node.attr("value");
            if (StringUtils.isNotEmpty(productName)) {
                getProductIds.add(productName);
            }
        }
        logger.info("SelectOptions获取结束");
        return getProductIds;
    }

    private Integer getQty(OkHttpClient client, String mallOrderId, String mallPId) throws Exception {
        Integer qty = 0;
        String url = "http://jx.10086.cn/jxb2b2/mobileImei/queryImeiNum";
        RequestBody body = new FormBody.Builder()
                .add("orderId", mallOrderId)
                .add("pId", mallPId)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        String[] list = StringUtils.split(result, CharConstant.SEMICOLON);
        logger.info("Qty获取结束");
        response.close();
        return Integer.valueOf(list[2]);
    }

    private String importOneMobileImei(OkHttpClient client, String ime, String orderId, String productId) throws IOException {
        String url = "http://jx.10086.cn/jxb2b2/mobileImei/importOneMobileImei.do";
        RequestBody body = new FormBody.Builder()
                .add("imei", ime)
                .add("orderId", orderId)
                .add("pId", productId)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        String msg = response.body().string();
        response.close();
        return msg;
    }

    private String checkOrder(OkHttpClient client, String mallOrderId) throws IOException {
        String url = "http://jx.10086.cn/jxb2b2/mobileImei/cheOrder";
        RequestBody body = new FormBody.Builder()
                .add("orderId", mallOrderId)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        String msg = response.body().string();
        logger.info("checkOrder获取结束");
        response.close();
        return msg;
    }

    private void sendHuo(OkHttpClient client, String mallOrderId, List<B2bOrderDetailDto> b2bOrderDetails, String deliveryNo, String deliveryCompan) throws IOException {
        String url = "http://jx.10086.cn/jxb2b2/mobileImei/sendHuo";
        FormBody.Builder builder = new FormBody.Builder();
        for (B2bOrderDetailDto b2bOrderDetail : b2bOrderDetails) {
            for (String ime : b2bOrderDetail.getImes()) {
                builder.add("imeiVal", ime + "-" + b2bOrderDetail.getMallProductId());
            }
        }
        builder.add("orderId", mallOrderId);
        builder.add("deliveryNo", deliveryNo);
        builder.add("deliveryCompan", deliveryCompan);
        builder.add("sendTime", LocalDateUtils.format(LocalDate.now().minusDays(1)));
        builder.add("remark", "1");
        builder.add("sendType", "0");
        builder.add("tag", "0");
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        logger.info("sendHuo获取结束");
        response.close();
    }

    private B2bOrderDto getTuiB2bOrder(OkHttpClient client, String code) throws Exception {
        B2bOrderDto b2bOrder = new B2bOrderDto();
        String url = "http://jx.10086.cn/jxb2b2/order/querySellerOrderList?&orderNo=" + code;
        String html = getOrderQueryHtml(client, url);
        Document document = Jsoup.parse(html);
        Element parent_mallOrder_span = document.getElementById("sp");
        String parentMallOrderId = parent_mallOrder_span.text().replace(",", "");
        if (code.contains("DDC")) {
            String ddcPage = getDDCPage(client, parentMallOrderId);
            Document ddcdocument = Jsoup.parse(ddcPage);
            Elements trs = ddcdocument.getElementsByClass("b_newTable").get(0).getElementsByTag("tr");
            boolean startSpider = false;
            for (Element tr : trs) {
                if (!tr.hasClass("myorderbox")) {
                    if (tr.getElementsByTag("td").get(0).getElementsByTag("span").get(0).getElementsByTag("a").get(0).text().contains(code)) {
                        startSpider = true;
                    } else {
                        startSpider = false;
                    }
                } else if (startSpider) {
                    if (tr.text().contains("订单推送") || b2bOrder.getStatus()) {
                        b2bOrder.setStatus(true);
                        Elements tds = tr.getElementsByTag("td");
                        B2bOrderDetailDto b2bOrderDetail = new B2bOrderDetailDto();
                        b2bOrderDetail.setMallProductTypeAndColor(tds.get(1).text() + tds.get(3).text());
                        b2bOrderDetail.setQty(Double.valueOf(tds.get(5).text()).intValue());
                        b2bOrder.getB2bOrderDetailList().add(b2bOrderDetail);
                    } else {
                        startSpider = false;
                    }
                }
            }
        } else if (code.contains("DD")) {
            Elements trs = document.getElementsByClass("b_newTable").get(0).getElementsByTag("tr");
            Element element = trs.get(2);
            if (element.text().contains("订单推送") || b2bOrder.getStatus()) {
                b2bOrder.setStatus(true);
                for (Element tr : trs) {
                    if (tr.hasClass("myorderbox")) {
                        Elements tds = tr.getElementsByTag("td");
                        B2bOrderDetailDto b2bOrderDetail = new B2bOrderDetailDto();
                        b2bOrderDetail.setMallProductTypeAndColor(tds.get(1).text() + tds.get(3).text());
                        b2bOrderDetail.setQty(Double.valueOf(tds.get(5).text()).intValue());
                        b2bOrder.getB2bOrderDetailList().add(b2bOrderDetail);
                    }
                }
            }
        }
        return b2bOrder;
    }

    private String tsKjOrder(OkHttpClient client, String mallOrderId) throws Exception {
        String url = "http://jx.10086.cn/jxb2b2/order/pushOrderKJ";
        RequestBody requestBody = new FormBody.Builder()
                .add("orderId", mallOrderId)
                .add("addrCode", "1122")
                .build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        String msg = response.body().string();
        logger.info("推送完成");
        response.close();
        return msg;
    }


    private OkHttpClient getClient(String username) {
        if (!okHttpClientMap.containsKey(username)) {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS).readTimeout(10000, TimeUnit.MILLISECONDS).writeTimeout(10000, TimeUnit.MILLISECONDS).cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    Map<String, Cookie> cookieMap = Maps.newHashMap();
                    if (cookieStore.containsKey(url.host())) {
                        for (Cookie cookie : cookieStore.get(url.host())) {
                            cookieMap.put(cookie.name() + cookie.path(), cookie);
                        }
                    }
                    for (Cookie cookie : cookies) {
                        cookieMap.put(cookie.name() + cookie.path(), cookie);
                    }
                    cookieStore.put(url.host(), Lists.newArrayList(cookieMap.values()));
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            }).build();
            okHttpClientMap.put(username, client);
        }
        return okHttpClientMap.get(username);
    }
}
