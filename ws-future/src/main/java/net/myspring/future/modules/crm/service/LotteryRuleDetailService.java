package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.basic.repository.LotteryRuleDetailRepository;
import net.myspring.future.modules.crm.domain.LotteryRuleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LotteryRuleDetailService {

    @Autowired
    private LotteryRuleDetailRepository lotteryRuleDetailRepository;

    public LotteryRuleDetail findOne(String id){
        LotteryRuleDetail lotteryRuleDetail=lotteryRuleDetailRepository.findOne(id);
        return lotteryRuleDetail;
    }
}
