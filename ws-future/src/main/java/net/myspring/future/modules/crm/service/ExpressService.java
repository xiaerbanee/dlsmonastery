package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.dto.ExpressDto;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.ExpressRepository;
import net.myspring.future.modules.crm.web.form.ExpressForm;
import net.myspring.future.modules.crm.web.query.ExpressQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ExpressService {

    @Autowired
    private ExpressRepository expressRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private RedisTemplate redisTemplate;


    public Page<ExpressDto> findPage(Pageable pageable, ExpressQuery expressQuery) {
        Page<ExpressDto> page = expressRepository.findPage(pageable, expressQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void logicDelete(String expressId) {
        expressRepository.logicDelete(expressId);
    }

    @Transactional
    public Express save(ExpressForm expressForm) {

        ExpressOrder eo = saveExpressOrderWithoutSettingExpressCodes(expressForm);

        Express  express;
        if(expressForm.isCreate()){
            express = new Express();
        }else{
            express = expressRepository.findOne(expressForm.getId());
        }
        ReflectionUtil.copyProperties(expressForm, express);
        express.setExpressOrderId(eo.getId());
        express.setShouldPay(caculateShouldPay(express));

        expressRepository.save(express);


        if(express.getExpressOrderId() !=null) {
            reCalcAndUpdateExpressCodes(express.getExpressOrderId());
        }

        return express;
    }

    @Transactional
    private ExpressOrder reCalcAndUpdateExpressCodes(String expressOrderId) {

        ExpressOrder eo = expressOrderRepository.findOne(expressOrderId);
        List<Express> expressList = expressRepository.findByEnabledIsTrueAndExpressOrderId(expressOrderId);
        eo.setExpressCodes(StringUtils.join(CollectionUtil.extractToList(expressList, "code"), CharConstant.COMMA));
        expressOrderRepository.save(eo);
        return eo;
    }

    private BigDecimal caculateShouldPay(Express express) {
        //计算快递运费

        if(express.getWeight()==null){
            return null;
        }
        Map<String, Object> matchingRule = findMatchingRuleForExpressShouldPay(express.getWeight());
        if(matchingRule == null){
            return null;
        }

        BigDecimal shouldPay = new BigDecimal(String.valueOf(matchingRule.get("price")));
        Object app = matchingRule.get("addPerPrice");
        if (app != null) {
            BigDecimal addPerPrice = new BigDecimal(String.valueOf(app));
            BigDecimal sub = (express.getWeight().subtract(new BigDecimal(String.valueOf(matchingRule.get("min"))))).setScale(2, BigDecimal.ROUND_UP);
            shouldPay = shouldPay.add(sub.multiply(addPerPrice)).setScale(1, BigDecimal.ROUND_UP);
        }
        return shouldPay;
    }

    private Map<String,Object> findMatchingRuleForExpressShouldPay(BigDecimal weight) {
        CompanyConfigCacheDto companyConfigCacheDto = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.EXPRESS_SHOULD_PAY_RULE.name());
       if(companyConfigCacheDto == null || StringUtils.isBlank(companyConfigCacheDto.getValue())){
            return null;
        }

        List<Map<String, Object>> list = ObjectMapperUtils.readValue(companyConfigCacheDto.getValue(), List.class);
        if(list == null){
            return null;
        }

        Optional<Map<String, Object>> optionalMatchingRule = list.stream().filter(s -> (weight.compareTo(new BigDecimal(String.valueOf(s.get("min")))) >= 0 && weight.compareTo(new BigDecimal(String.valueOf(s.get("max")))) <= 0)).limit(1).findFirst();
        return optionalMatchingRule.orElse(null);

    }

    @Transactional
    private ExpressOrder saveExpressOrderWithoutSettingExpressCodes(ExpressForm expressForm) {

        if(expressForm.getExpressOrderToDepotId()  == null){
            throw new ServiceException("errorToDepotIdCantBeNull");
        }

        ExpressOrder expressOrder;
        if (expressForm.getExpressOrderId() == null) {
            Depot toDepot = depotRepository.findOne(expressForm.getExpressOrderToDepotId());
            expressOrder = new ExpressOrder();
            expressOrder.setToDepotId(expressForm.getExpressOrderToDepotId());
            expressOrder.setContator(toDepot.getContator());
            expressOrder.setMobilePhone(toDepot.getMobilePhone());
            expressOrder.setAddress(toDepot.getAddress());
            expressOrder.setExtendType(ExpressOrderTypeEnum.手机订单.name());
            expressOrder.setPrintDate(LocalDate.now());
            expressOrder.setExpressCompanyId(getDefaultExpressCompanyId());
            expressOrder.setExpressPrintQty(0);
        } else {
            expressOrder = expressOrderRepository.findOne(expressForm.getExpressOrderId());
        }

        return expressOrderRepository.save(expressOrder);
    }

    private String getDefaultExpressCompanyId() {
        String defaultExpressCompanyId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.DEFAULT_EXPRESS_COMPANY_ID.name()).getValue();
        return StringUtils.trimToNull(defaultExpressCompanyId);

    }

    public SimpleExcelBook export(ExpressQuery expressQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> expressColumnList = Lists.newArrayList();
        expressColumnList.add(new SimpleExcelColumn(workbook, "code", "快递单号"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderExpressCompanyName", "快递公司"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderExtendType", "类型"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderExtendBusinessId", "订单编号"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderFromDepotName", "仓库"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderToDepotAreaName", "办事处"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderToDepotName", "门店"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderContator", "收件人"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderMobilePhone", "手机"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderAddress", "地址"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "weight", "重量"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "shouldPay", "应付"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "realPay", "实付"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "qty", "台数"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "expressOrderTotalQty", "订单总台数"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedByName", "更新人"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "更新时间"));
        expressColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
        List<ExpressDto> expressDtoList = findPage(new PageRequest(0,10000), expressQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("快递单列表", expressDtoList, expressColumnList));

        ExcelUtils.doWrite(workbook,simpleExcelSheetList);
        return new SimpleExcelBook(workbook,"快递单列表"+LocalDate.now()+".xlsx", simpleExcelSheetList);
    }

    public ExpressDto findDto(String id) {
        ExpressDto expressDto = expressRepository.findDto(id);
        cacheUtils.initCacheInput(expressDto);
        return  expressDto;
    }
}
