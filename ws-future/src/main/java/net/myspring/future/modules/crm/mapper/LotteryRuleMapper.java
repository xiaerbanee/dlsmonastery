package net.myspring.future.modules.crm.mapper;


import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.LotteryRule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LotteryRuleMapper extends MyMapper<LotteryRule,String> {

}
