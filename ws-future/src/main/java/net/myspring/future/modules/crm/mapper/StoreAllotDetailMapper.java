package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreAllotDetailMapper extends MyMapper<StoreAllotDetail,String> {

    List<StoreAllotDetail> findByStoreAllotIds(List<String> storeAllotIds);

}
