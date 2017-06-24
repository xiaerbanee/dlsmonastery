package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.SalOutStockDto;
import net.myspring.cloud.modules.input.dto.SalOutStockFEntityDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.cloud.modules.kingdee.domain.StkInventory;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.web.form.DepotAdApplyForm;
import net.myspring.future.modules.basic.web.form.ProductAdForm;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.layout.domain.AdApply;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdApplyDto;
import net.myspring.future.modules.layout.repository.AdApplyRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderRepository;
import net.myspring.future.modules.layout.web.form.*;
import net.myspring.future.modules.layout.web.query.AdApplyQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AdApplyService {
    @Autowired
    private AdApplyRepository adApplyRepository;
    @Autowired
    private AdGoodsOrderRepository adGoodsOrderRepository;
    @Autowired
    private AdGoodsOrderDetailRepository adGoodsOrderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private CloudClient cloudClient;

    public Page<AdApplyDto> findPage(Pageable pageable, AdApplyQuery adApplyQuery) {
        Page<AdApplyDto> page = adApplyRepository.findPage(pageable, adApplyQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public AdApplyDto findOne(String id){
        AdApplyDto adApplyDto;
        if(StringUtils.isBlank(id)){
            adApplyDto = new AdApplyDto();
        } else {
            AdApply adApply= adApplyRepository.findOne(id);
            adApplyDto = BeanUtil.map(adApply,AdApplyDto.class);
            cacheUtils.initCacheInput(adApplyDto);
        }
        return adApplyDto;
    }

    public AdApplyForm getForm(AdApplyForm adApplyForm){
        List<String> billTypes = Lists.newArrayList();
        billTypes.add(BillTypeEnum.POP.name());
        billTypes.add(BillTypeEnum.配件赠品.name());
        adApplyForm.getExtra().put("billTypes",billTypes);
        return adApplyForm;
    }

    public AdApplyBillForm getBillForm(AdApplyBillForm adApplyBillForm){
        List<String> billTypes = Lists.newArrayList();
        billTypes.add(BillTypeEnum.POP.name());
        billTypes.add(BillTypeEnum.配件赠品.name());
        adApplyBillForm.getExtra().put("billTypes",billTypes);
        return adApplyBillForm;
    }

    public AdApplyBillTypeChangeForm findAdApplyList(String billType){
        AdApplyBillTypeChangeForm adApplyBillTypeChangeForm = new AdApplyBillTypeChangeForm();
        List<String> outGroupIds = Lists.newArrayList();
        if(BillTypeEnum.POP.name().equalsIgnoreCase(billType)){
            outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_POP_GROUP_IDS.name()).getValue());
            adApplyBillTypeChangeForm.setStoreId(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.AD_DEFAULT_STORE_ID.name()).getValue());
        }
        if(BillTypeEnum.配件赠品.name().equalsIgnoreCase(billType)){
            outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_GOODS_POP_GROUP_IDS.name()).getValue());
            adApplyBillTypeChangeForm.setStoreId(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue());
        }
        LocalDate dateStart = LocalDate.now().plusYears(-1);
        List<AdApplyDto> adApplyDtos = adApplyRepository.findByOutGroupIdAndDate(dateStart,outGroupIds);
        //同步财务库存
       /* Map<String,AdApplyDto> adApplyDtoMap = CollectionUtil.extractToMap(adApplyDtos,"productId");
        List<String> productIds = CollectionUtil.extractToList(productRepository.findAll(),"outId");
        List<StkInventory> stkInventories = cloudClient.findInventoryByProductIds(productIds);
        for(StkInventory stkInventory:stkInventories){
            adApplyDtoMap.get(stkInventory.getFMaterialId()).setStoreQty(stkInventory.getFBaseQty());
        }*/
        cacheUtils.initCacheInput(adApplyDtos);
        adApplyBillTypeChangeForm.setAdApplyDtoList(adApplyDtos);
        return adApplyBillTypeChangeForm;
    }

    public AdApplyGoodsForm getAdApplyGoodsList(AdApplyGoodsForm adApplyGoodsForm){
        List<DepotAdApplyForm> depotAdApplyForms = BeanUtil.map(depotRepository.findByEnabledIsTrueAndAdShopIsTrueAndIsHiddenIsFalse(),DepotAdApplyForm.class);
        adApplyGoodsForm.setDepotAdApplyForms(depotAdApplyForms);
        return adApplyGoodsForm;
    }

    public void save(AdApplyForm adApplyForm){
        if(CollectionUtil.isEmpty(adApplyForm.getProductAdForms())){
            return;
        }
        List<AdApply> adApplyList = Lists.newArrayList();
        for(ProductAdForm productAdForm:adApplyForm.getProductAdForms()){
            Integer applyQty = productAdForm.getApplyQty();
            if(applyQty!=null&&applyQty>0){
                AdApply adApply = new AdApply();
                adApply.setApplyQty(applyQty);
                adApply.setConfirmQty(applyQty);
                adApply.setBilledQty(0);
                adApply.setLeftQty(applyQty);
                adApply.setShopId(adApplyForm.getShopId());
                adApply.setProductId(productAdForm.getId());
                adApply.setRemarks(adApplyForm.getRemarks());
                adApply.setExpiryDateRemarks(productAdForm.getExpiryDateRemarks());
                adApplyList.add(adApply);
            }
        }
        adApplyRepository.save(adApplyList);
    }

    public void goodsSave(AdApplyGoodsForm adApplyGoodsForm){
        if(adApplyGoodsForm.getDepotAdApplyForms() == null){
            return;
        }
        Product product = productRepository.findOne(adApplyGoodsForm.getProductId());
        if(product == null){
            return;
        }
        List<AdApply> adApplyList = Lists.newArrayList();
        for (DepotAdApplyForm depotAdApplyForm:adApplyGoodsForm.getDepotAdApplyForms()){
            AdApply adApply = new AdApply();
            adApply.setProductId(adApplyGoodsForm.getProductId());
            adApply.setShopId(depotAdApplyForm.getId());
            adApply.setApplyQty(depotAdApplyForm.getApplyQty());
            adApply.setConfirmQty(depotAdApplyForm.getApplyQty());
            adApply.setBilledQty(0);
            adApply.setLeftQty(depotAdApplyForm.getApplyQty());
            adApply.setExpiryDateRemarks(product.getExpiryDateRemarks());
            adApply.setRemarks(adApplyGoodsForm.getRemarks());
            adApplyList.add(adApply);
        }
        adApplyRepository.save(adApplyList);
    }

    public void billSave(AdApplyBillForm adApplyBillForm){
        List<String> adApplyId  =CollectionUtil.extractToList(adApplyBillForm.getAdApplyDetailForms(),"id");
        Map<String,AdApplyDetailForm> adApplyDetailFormMap = CollectionUtil.extractToMap(adApplyBillForm.getAdApplyDetailForms(),"id");
        List<AdApply> adApplyList = adApplyRepository.findAll(adApplyId);
        Map<String,AdGoodsOrder> adGoodsOrderMap = Maps.newHashMap();
        List<AdGoodsOrderDetail> adGoodsOrderDetails = Lists.newArrayList();
        List<AdGoodsOrder> adGoodsOrders = Lists.newArrayList();
        if(adApplyList == null){
            return;
        }
        //生成AdGoodsOrder
        for(AdApply adApply:adApplyList){
            if(!adGoodsOrderMap.containsKey(adApply.getShopId())){
                AdGoodsOrder adGoodsOrder = new AdGoodsOrder();
                adGoodsOrder.setBillType(adApplyBillForm.getBillType());
                adGoodsOrder.setStoreId(adApplyBillForm.getStoreId());
                adGoodsOrder.setOutShopId(adApply.getShopId());
                adGoodsOrder.setShopId(adApply.getShopId());
                adGoodsOrder.setBillDate(adApplyBillForm.getBillDate());
                adGoodsOrder.setSmallQty(0);
                adGoodsOrder.setMediumQty(0);
                adGoodsOrder.setLargeQty(0);
                adGoodsOrder.setSplitBill(false);
                adGoodsOrder.setLocked(true);
                adGoodsOrder.setBillRemarks(adApplyBillForm.getRemarks());
                adGoodsOrderMap.put(adApply.getShopId(), adGoodsOrder);
                adGoodsOrderRepository.save(adGoodsOrder);
            }
            AdGoodsOrder adGoodsOrder = adGoodsOrderMap.get(adApply.getShopId());
            adGoodsOrders.add(adGoodsOrder);
            Product product = productRepository.findOne(adApply.getProductId());
            AdGoodsOrderDetail adGoodsOrderDetail = new AdGoodsOrderDetail();
            adGoodsOrderDetail.setAdGoodsOrderId(adGoodsOrder.getId());
            adGoodsOrderDetail.setProductId(adApply.getProductId());
            adGoodsOrderDetail.setPrice(product.getPrice2());
            adGoodsOrderDetail.setShouldGet(product.getShouldGet());
            adGoodsOrderDetail.setShouldPay(BigDecimal.ZERO);
            adGoodsOrderDetail.setShippedQty(0);
            adGoodsOrderDetail.setQty(adApply.getApplyQty());
            adGoodsOrderDetail.setConfirmQty(adApply.getConfirmQty());
            adGoodsOrderDetail.setBillQty(adApplyDetailFormMap.get(adApply.getId()).getNowBilledQty());
            adGoodsOrderDetails.add(adGoodsOrderDetail);
        }
        adGoodsOrderDetailRepository.save(adGoodsOrderDetails);

        //自动开单
        String maxBusinessId = adGoodsOrderRepository.findMaxBusinessId(adApplyBillForm.getBillDate());
        for(AdGoodsOrder adGoodsOrder:adGoodsOrders){

            adGoodsOrder.setBusinessId(IdUtils.getNextBusinessId(maxBusinessId));
            BigDecimal amount = BigDecimal.ZERO;
            List<AdGoodsOrderDetail> adGoodsOrderDetailLists = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrder.getId());
            for(AdGoodsOrderDetail adGoodsOrderDetail:adGoodsOrderDetailLists){
                amount = amount.add(adGoodsOrderDetail.getPrice().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
            }
            adGoodsOrder.setAmount(amount);
            adGoodsOrder.setProcessStatus(GoodsOrderStatusEnum.待发货.name());
            Depot depot = depotRepository.findOne(adGoodsOrder.getShopId());
            ExpressOrder expressOrder = new ExpressOrder();
            expressOrder.setExtendBusinessId(adGoodsOrder.getBusinessId());
            expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
            expressOrder.setExtendId(adGoodsOrder.getId());
            expressOrder.setFromDepotId(adGoodsOrder.getStoreId());
            expressOrder.setExpressCompanyId(adApplyBillForm.getExpressCompanyId());
            expressOrder.setToDepotId(adGoodsOrder.getShopId());
            expressOrder.setContator(depot.getContator());
            expressOrder.setAddress(depot.getAddress());
            expressOrder.setMobilePhone(depot.getMobilePhone());
            expressOrder.setPrintDate(adApplyBillForm.getBillDate());
            if(BillTypeEnum.POP.name().equalsIgnoreCase(adApplyBillForm.getBillType())){
                expressOrder.setExpressPrintQty(0);
            }else{
                expressOrder.setExpressPrintQty(1);
            }

            expressOrderRepository.save(expressOrder);
            adGoodsOrder.setExpressOrderId(expressOrder.getId());
            adGoodsOrderRepository.save(adGoodsOrder);
        }

        //保存adApply
        List<AdApply> newAdApplys = Lists.newArrayList();
        for(AdApply adApply:adApplyList){
            AdGoodsOrder adGoodsOrder = adGoodsOrderMap.get(adApply.getShopId());
            AdApplyDetailForm adApplyDetailForm = adApplyDetailFormMap.get(adApply.getId());
            adApply.setBilledQty(adApply.getBilledQty()+adApplyDetailForm.getNowBilledQty());
            if(StringUtils.isBlank(adApply.getOrderId())){
                adApply.setOrderId(adGoodsOrder.getId());
            }else{
                adApply.setOrderId(adApply.getOrderId()+ CharConstant.COMMA+adGoodsOrder.getId());
            }
            adApply.setLeftQty(adApply.getConfirmQty()-adApply.getBilledQty());
            newAdApplys.add(adApply);
        }
        adApplyRepository.save(newAdApplys);

        //TODO 调用金蝶接口
       batchSynToCloud(adGoodsOrders);
    }

    private List<KingdeeSynReturnDto> batchSynToCloud(List<AdGoodsOrder> adGoodsOrderList){
        List<SalOutStockDto> salOutStockDtoList = Lists.newArrayList();
        Map<String,Depot> depotMap = CollectionUtil.extractToMap(depotRepository.findAll(),"id");
        Map<String,Client> clientMap = CollectionUtil.extractToMap(clientRepository.findAll(),"id");
        Map<String,Product> productMap = CollectionUtil.extractToMap(productRepository.findAll(),"id");
        for (AdGoodsOrder adGoodsOrder : adGoodsOrderList){
            Depot outShop = depotMap.get(adGoodsOrder.getOutShopId());
            Client client = clientMap.get(outShop.getClientId());
            if(client == null || StringUtils.isBlank(client.getOutId())){
                throw new ServiceException(client.getName() + " 没有关联财务客户，不能申请");
            }
            if(outShop.getDepotStoreId() == null || StringUtils.isBlank(outShop.getDepotStoreId())){
                throw new ServiceException(client.getName() + " 没有关联财务仓库，不能申请");
            }
            DepotStore depotstore = depotStoreRepository.findOne(outShop.getDepotStoreId());
            SalOutStockDto salOutStockDto = new SalOutStockDto();
            salOutStockDto.setExtendId(adGoodsOrder.getId());
            salOutStockDto.setExtendType(ExtendTypeEnum.POP征订.name());
            salOutStockDto.setDate(adGoodsOrder.getBillDate());
            salOutStockDto.setCustomerNumber(client.getOutCode());
            salOutStockDto.setNote(adGoodsOrder.getId()+CharConstant.COMMA+outShop.getName()+CharConstant.COMMA+outShop.getContator()
                    +CharConstant.COMMA+outShop.getMobilePhone()+CharConstant.COMMA+outShop.getAddress()+CharConstant.COMMA+adGoodsOrder.getRemarks());
            List<SalOutStockFEntityDto> entityDtoList = Lists.newArrayList();
            List<AdGoodsOrderDetail> adGoodsOrderDetailLists = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrder.getId());
            for(AdGoodsOrderDetail adGoodsOrderDetail:adGoodsOrderDetailLists){
                Product product = productMap.get(adGoodsOrderDetail.getProductId());
                SalOutStockFEntityDto entityDto = new SalOutStockFEntityDto();
                entityDto.setStockNumber(depotstore.getOutCode());
                entityDto.setMaterialNumber(product.getCode());
                entityDto.setQty(adGoodsOrderDetail.getBillQty());
                // 是否赠品 赠品，电教，imoo 广告办事处的以原价出库
                if(BillTypeEnum.配件赠品.name().equals(adGoodsOrder.getBillType())){//或者是电教公司,而且的depot必须是金蝶里有的
                    entityDto.setPrice(product.getPrice2());
                }else{
                    entityDto.setPrice(BigDecimal.ZERO);
                }
                entityDto.setEntryNote(adGoodsOrder.getId()+CharConstant.COMMA+outShop.getName()+CharConstant.COMMA+outShop.getContator()
                        +CharConstant.COMMA+outShop.getMobilePhone()+CharConstant.COMMA+outShop.getAddress());
                entityDtoList.add(entityDto);
            }
            salOutStockDto.setSalOutStockFEntityDtoList(entityDtoList);
            salOutStockDtoList.add(salOutStockDto);
        }
        return cloudClient.synSalOutStock(salOutStockDtoList);

    }

    public String findSimpleExcelSheets(Workbook workbook,AdApplyQuery adApplyQuery){
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "id", "编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productCode", "物料编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productName", "物料名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "applyQty", "申请数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "confirmQty", "确认数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "leftQty", "待开单数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "expiryDateRemarks", "截止日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

        List<AdApplyDto> adApplyDtos = adApplyRepository.findByFilter(adApplyQuery);
        cacheUtils.initCacheInput(adApplyDtos);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("POP征订", adApplyDtos, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"POP征订"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());
    }
}
