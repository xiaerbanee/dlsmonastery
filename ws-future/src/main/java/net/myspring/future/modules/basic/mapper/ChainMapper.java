package net.myspring.future.modules.basic.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.dto.ChainDto;
import net.myspring.future.modules.basic.web.query.ChainQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ChainMapper extends MyMapper<Chain,String> {

    Page<ChainDto> findPage(Pageable pageable, @Param("p")ChainQuery chainQuery);

    List<Chain> findLabels(List<String> ids);

    int deleteByChainId(String chainId);

    int saveAccountAndChain(@Param("chainId") String chainId, @Param("accountIdList") List<String> accountIdList);
}
