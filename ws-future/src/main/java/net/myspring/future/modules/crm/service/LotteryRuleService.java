package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.basic.repository.LotteryRuleRepository;
import net.myspring.future.modules.crm.domain.LotteryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LotteryRuleService {

    @Autowired
    private LotteryRuleRepository lotteryRuleRepository;

    public LotteryRule findOne(String id){
        LotteryRule lotteryRule=lotteryRuleRepository.findOne(id);
        return lotteryRule;
    }
}
