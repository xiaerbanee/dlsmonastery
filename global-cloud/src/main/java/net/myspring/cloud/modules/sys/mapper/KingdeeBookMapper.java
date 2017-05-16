package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.common.mybatis.MyMapper;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.web.query.KingdeeBookQuery;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface KingdeeBookMapper extends MyMapper<KingdeeBook,String> {

    Page<KingdeeBook> findPage(Pageable pageable, @Param("p")KingdeeBookQuery kingdeeBookQuery);

    KingdeeBook findByAccountId(String accountId);

    List<String> findNames();

    List<String> findTypes();
}
