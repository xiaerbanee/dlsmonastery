package net.myspring.mybatis.mapper;

import net.myspring.mybatis.provider.CrudProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;

/**
 * Created by liuj on 2016/11/14.
 */
public interface CrudMapper<T, ID extends Serializable> extends BaseMapper<T, ID> {

    @InsertProvider(type=CrudProvider.class, method = CrudProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(T entity);

    @UpdateProvider(type=CrudProvider.class, method = CrudProvider.UPDATE)
    int update(T entity);

    @SelectProvider(type=CrudProvider.class,method = CrudProvider.FIND_ONE)
    T findOne(ID id);

    @UpdateProvider(type=CrudProvider.class,method = CrudProvider.DELETE_ONE)
    int deleteOne(ID id);

    @UpdateProvider(type=CrudProvider.class,method = CrudProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(ID id);
}
