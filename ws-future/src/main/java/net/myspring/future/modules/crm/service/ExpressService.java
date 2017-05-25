package net.myspring.future.modules.crm.service;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.ExpressRepository;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.dto.ExpressDto;
import net.myspring.future.modules.crm.mapper.ExpressMapper;
import net.myspring.future.modules.crm.web.form.ExpressForm;
import net.myspring.future.modules.crm.web.query.ExpressQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ExpressService {

    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private ExpressRepository expressRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;

    public Page<ExpressDto> findPage(Pageable pageable, ExpressQuery expressQuery) {
        Page<ExpressDto> page = expressMapper.findPage(pageable, expressQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ExpressForm getForm(ExpressForm expressForm) {
        if(expressForm.isCreate()){
            return  new ExpressForm();
        }

        ExpressDto expressDto = expressRepository.findDto(expressForm.getId());
        cacheUtils.initCacheInput(expressDto);
        ExpressForm result = BeanUtil.map(expressDto, ExpressForm.class);

        return result;
    }

    public void logicDeleteOne(String expressId) {
        expressRepository.logicDeleteOne(expressId);
    }


    public Express saveOrUpdate(ExpressForm expressForm) {

        ExpressOrder eo = saveOrUpdateExpressOrderWithoutSettingExpressCodes(expressForm);

        Express  express = null;
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


    private ExpressOrder reCalcAndUpdateExpressCodes(String expressOrderId) {

        ExpressOrder eo = expressOrderRepository.findOne(expressOrderId);
        List<Express> expressList = expressRepository.findByExpressOrderId(expressOrderId);
        eo.setExpressCodes(StringUtils.join(CollectionUtil.extractToList(expressList, "code"), CharConstant.COMMA));
        expressOrderRepository.save(eo);
        return eo;
    }

    private BigDecimal caculateShouldPay(Express express) {
        //计算快递运费

        if(express.getWeight()==null){
            return null;
        }
        Map<String, Object> matchingRule = findMatchingRuleForExpressShouldGet(express.getWeight());
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

    private Map<String,Object> findMatchingRuleForExpressShouldGet(BigDecimal weight) {
        String rule = null;//TODO CompanyConfigUtil.hasExpressShouldPayRule();Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfigCode.EXPRESS_SHOULD_PAY_RULE.getCode());
        if(StringUtils.isBlank(rule)){
            return null;
        }

        List<Map<String, Object>> list = ObjectMapperUtils.readValue(rule, List.class);
        if(list == null){
            return null;
        }

        Optional<Map<String, Object>> optionanlMatchingRule = list.stream().filter(s -> (weight.compareTo(new BigDecimal(String.valueOf(s.get("min")))) >= 0 && weight.compareTo(new BigDecimal(String.valueOf(s.get("max")))) <= 0)).limit(1).findFirst();
        if(optionanlMatchingRule.isPresent()){
            return optionanlMatchingRule.get();
        }else {
            return null;
        }

    }

    private ExpressOrder saveOrUpdateExpressOrderWithoutSettingExpressCodes(ExpressForm expressForm) {

        if(expressForm.getExpressOrderToDepotId()  == null){
            throw new ServiceException("errorToDepotIdCantBeNull");
        }

        ExpressOrder expressOrder = null;
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
        } else {
            expressOrder = expressOrderRepository.findOne(expressForm.getExpressOrderId());
        }

        return expressOrderRepository.save(expressOrder);
    }

    private String getDefaultExpressCompanyId() {
        //TODO default expressCompanyID
//        String code = Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfig.CompanyConfigCode.DEFAULT_EXPRESS_COMPANY_ID.getCode());
//        if (StringUtils.isNotBlank(code)) {
//            ExpressCompany expressCompany = expressCompanyDao.findOne(Long.valueOf(code));
//            storeAllotForm.setExpressCompanyId( expressCompanyService.getDefaultExpressCompanyId());
//        }

        return null;
    }

}
