package net.myspring.common.mybatis;

import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuj on 2017/3/27.
 */
public interface MyMapper<T, ID extends Serializable> extends CrudMapper<T, ID> {

    @UpdateProvider(type=MyProvider.class,method = "logicDeleteOne")
    int logicDeleteOne(ID id);

    @UpdateProvider(type=MyProvider.class,method = "logicDeleteByIds")
    int logicDeleteByIds(List<ID> ids);

    @SelectProvider(type = MyProvider.class,method = "findAllEnabled")
    List<T> findAllEnabled();

}
