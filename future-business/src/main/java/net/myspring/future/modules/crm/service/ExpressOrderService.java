package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.mapper.ExpressCompanyMapper;
import net.myspring.future.modules.crm.mapper.ExpressMapper;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(readOnly=false)
@Service
public class ExpressOrderService {

    @Autowired
    private ExpressOrderMapper expressOrderMapper;
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private ExpressMapper expressMapper;

    public ExpressOrder findOne(String id){
        ExpressOrder expressOrder = expressOrderMapper.findOne(id);
        return expressOrder;
    }

    public Page<ExpressOrder> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ExpressOrder> page = expressOrderMapper.findPage(pageable, map);
        return page;
    }

    public void resetPrintStatus(ExpressOrder expressOrder){
        expressOrder.setExpressPrintDate(null);
        expressOrder.setOutPrintDate(null);
        expressOrderMapper.update(expressOrder);
    }

    public void update(ExpressOrder expressOrder){
        expressOrderMapper.update(expressOrder);
    }

    public void save(ExpressOrder expressOrder){
        expressOrderMapper.save(expressOrder);
    }

    public void save(String extendType,String extendId,String expressCodes,String expressCompanyId) {
        ExpressOrder expressOrder = expressOrderMapper.findByExtendIdAndType(extendId, extendType);
        expressOrder.setExpressCompanyId(expressCompanyId);
        expressOrder.setExpressCodes(expressCodes);

        List<String> expressCodeList = StringUtils.getSplitList(expressCodes, "");
        List<Express> expresses = expressMapper.findByExpressOrderId(expressOrder.getId());
        if(CollectionUtil.isNotEmpty(expresses)) {
            for(int i = expresses.size()-1;i>=0;i--) {
                Express express = expresses.get(i);
                if(!expressCodeList.contains(express.getCode())) {
                    express.setEnabled(false);
                    expressMapper.update(express);
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
                expressMapper.save(express);
            }
        }
        expressOrderMapper.update(expressOrder);
    }
}
