package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.LotteryRuleDetail;
import net.myspring.future.modules.crm.mapper.LotteryRuleDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotteryRuleDetailService {

    @Autowired
    private LotteryRuleDetailMapper lotteryRuleDetailMapper;

    public LotteryRuleDetail findOne(String id){
        LotteryRuleDetail lotteryRuleDetail=lotteryRuleDetailMapper.findOne(id);
        return lotteryRuleDetail;
    }
}
