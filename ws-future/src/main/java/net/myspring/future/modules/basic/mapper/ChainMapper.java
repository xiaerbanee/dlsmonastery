package net.myspring.future.modules.basic.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.Chain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChainMapper extends MyMapper<Chain,String> {

    Page<Chain> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<Chain> findLabels(List<String> ids);

    int deleteByChainId(String chainId);

    int saveAccountAndChain(@Param("chainId") String chainId, @Param("accountIdList") List<String> accountIdList);
}
