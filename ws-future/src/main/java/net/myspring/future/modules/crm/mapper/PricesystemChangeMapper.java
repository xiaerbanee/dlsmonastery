package net.myspring.future.modules.crm.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.crm.domain.PricesystemChange;
import net.myspring.future.modules.crm.dto.PricesystemChangeDto;
import net.myspring.future.modules.crm.web.form.PricesystemChangeForm;
import net.myspring.future.modules.crm.web.query.PricesystemChangeQuery;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;
import java.util.Map;

@Mapper
public interface PricesystemChangeMapper extends MyMapper<PricesystemChange,String> {

    Page<PricesystemChangeDto> findPage(Pageable pageable, @Param("p")PricesystemChangeQuery pricesystemChangeQuery);

    void audit(@Param("ids")String[] ids,@Param("pass") Boolean pass);

    void auditOperation(@Param("id") String id,@Param("pass") Boolean pass);

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    PricesystemChange findOne(String id);

    int updateRemark(@Param("p")PricesystemChangeForm pricesystemChangeForm);
}
