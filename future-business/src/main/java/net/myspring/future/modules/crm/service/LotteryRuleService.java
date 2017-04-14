package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.LotteryRule;
import net.myspring.future.modules.crm.mapper.LotteryRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotteryRuleService {

    @Autowired
    private LotteryRuleMapper lotteryRuleMapper;

    public LotteryRule findOne(String id){
        LotteryRule lotteryRule=lotteryRuleMapper.findOne(id);
        return lotteryRule;
    }
}
