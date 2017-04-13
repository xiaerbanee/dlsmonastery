package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AdPricesystemDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdPricesystemDetailMapper extends MyMapper<AdPricesystemDetail,String> {

    List<AdPricesystemDetail> findByAdPricesystemId(String adPricesystemId);

}
