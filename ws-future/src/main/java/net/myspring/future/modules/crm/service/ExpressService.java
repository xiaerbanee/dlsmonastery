package net.myspring.future.modules.crm.service;

import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.ExpressOrderExtendTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.JsonBuilder;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.dto.ExpressDto;
import net.myspring.future.modules.crm.mapper.ExpressMapper;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.future.modules.crm.web.form.ExpressForm;
import net.myspring.future.modules.crm.web.query.ExpressQuery;
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

@Service
@Transactional
public class ExpressService {

    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ExpressOrderMapper expressOrderMapper;
    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private ExpressCompanyService expressCompanyService;

    @Autowired
    private ExpressOrderService expressOrderService;

    public Express findOne(String id){
        Express express=expressMapper.findOne(id);
        return express;
    }

    public Page<ExpressDto> findPage(Pageable pageable, ExpressQuery expressQuery) {
        Page<ExpressDto> page = expressMapper.findPage(pageable, expressQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ExpressForm findForm(ExpressForm expressForm) {
        if(expressForm.isCreate()){
            return  new ExpressForm();
        }

        ExpressDto expressDto = expressMapper.findDto(expressForm.getId());
        cacheUtils.initCacheInput(expressDto);
        ExpressForm result = BeanUtil.map(expressDto, ExpressForm.class);

        return result;
    }

    public void delete(ExpressForm expressForm) {
        expressMapper.logicDeleteOne(expressForm.getId());
    }


    public Express saveOrUpdate(ExpressForm expressForm) {

        ExpressOrder eo = saveOrUpdateExpressOrderWithoutSettingExpressCodes(expressForm);

        Express  express = null;
        if(expressForm.isCreate()){
            express = new Express();
        }else{
            express = expressMapper.findOne(expressForm.getId());
        }
        ReflectionUtil.copyProperties(expressForm, express);
        express.setExpressOrderId(eo.getId());
        express.setShouldPay(caculateShouldPay(express));

        saveOrUpdate(express);

        if(express.getExpressOrderId() !=null) {
            expressOrderService.reCalcAndUpdateExpressCodes(express.getExpressOrderId());
        }

        return express;
    }

    private Express saveOrUpdate(Express express) {
        if(express ==null){
            return null;
        }
        if(StringUtils.isBlank(express.getId())){
            expressMapper.save(express);
        }else{
            expressMapper.update(express);
        }
        return express;
    }

    private BigDecimal caculateShouldPay(Express express) {
        //计算快递运费
        String rule = null;//TODO CompanyConfigUtil.hasExpressShouldPayRule();Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfigCode.EXPRESS_SHOULD_PAY_RULE.getCode());
       if(StringUtils.isBlank(rule) || express.getWeight() == null ){
           return null;
       }

        BigDecimal shouldPay = BigDecimal.ZERO;
            try {
                List<Map<String, Object>> list = JsonBuilder.getNonEmptyMapper().fromJson(rule, List.class);
                for (Map<String, Object> map : list) {
                    BigDecimal min = new BigDecimal(String.valueOf(map.get("min")));
                    BigDecimal max = new BigDecimal(String.valueOf(map.get("max")));
                    if (express.getWeight().compareTo(min) <= 0 || express.getWeight().compareTo(max) > 0) {
                        continue;
                    }

                    shouldPay = new BigDecimal(String.valueOf(map.get("price")));
                    BigDecimal addPerPrice = null;
                    Object app = map.get("addPerPrice");
                    if (app != null) {
                        addPerPrice = new BigDecimal(String.valueOf(app));
                    }
                    if (addPerPrice != null) {
                        BigDecimal sub = (express.getWeight().subtract(min)).setScale(2, BigDecimal.ROUND_UP);
                        shouldPay = shouldPay.add(sub.multiply(addPerPrice)).setScale(1, BigDecimal.ROUND_UP);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("exception_express_price_count_fail");
            }

        return shouldPay;
    }

    private ExpressOrder saveOrUpdateExpressOrderWithoutSettingExpressCodes(ExpressForm expressForm) {

        if(expressForm.getExpressOrderToDepotId()  == null){
            throw new ServiceException("errorToDepotIdCantBeNull");
        }

        ExpressOrder expressOrder = null;
        if (expressForm.getExpressOrderId() == null) {
            Depot toDepot = depotMapper.findOne(expressForm.getExpressOrderToDepotId());
            expressOrder = new ExpressOrder();
            expressOrder.setToDepotId(expressForm.getExpressOrderToDepotId());
            expressOrder.setContator(toDepot.getContator());
            expressOrder.setMobilePhone(toDepot.getMobilePhone());
            expressOrder.setAddress(toDepot.getAddress());
            expressOrder.setExtendType(ExpressOrderExtendTypeEnum.手机订单.name());
            expressOrder.setPrintDate(LocalDate.now());
            expressOrder.setExpressCompanyId(expressCompanyService.getDefaultExpressCompanyId());
        } else {
            expressOrder = expressOrderService.findOne(expressForm.getExpressOrderId());
        }


        return expressOrderService.saveOrUpdate(expressOrder);
    }

}
