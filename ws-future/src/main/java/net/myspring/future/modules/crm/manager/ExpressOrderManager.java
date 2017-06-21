package net.myspring.future.modules.crm.manager;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.ExpressRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ExpressOrderManager {
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private ExpressRepository expressRepository;

    public void save(String extendType,String extendId,String expressCodes,String expressCompanyId) {
        ExpressOrder expressOrder = expressOrderRepository.findByExtendIdAndExtendType(extendId, extendType);
        expressOrder.setExpressCompanyId(expressCompanyId);
        expressOrder.setExpressCodes(expressCodes);

        List<String> expressCodeList = StringUtils.getSplitList(expressCodes, CharConstant.ENTER);
        List<Express> expresses = expressRepository.findByEnabledIsTrueAndExpressOrderId(expressOrder.getId());
        if(CollectionUtil.isNotEmpty(expresses)) {
            for(int i = expresses.size()-1;i>=0;i--) {
                Express express = expresses.get(i);
                if(!expressCodeList.contains(express.getCode())) {
                    express.setEnabled(false);
                    expressRepository.save(express);
                    expresses.remove(i);
                }
            }
        }
        Map<String,Express> expressMap = CollectionUtil.extractToMap(expresses, "code");
        for(String code:expressCodeList) {
            if(!expressMap.containsKey(code)){
                Express express = new Express();
                express.setExpressOrderId(expressOrder.getId());
                express.setCode(code);
                expressRepository.save(express);
            }
        }
    }


}
