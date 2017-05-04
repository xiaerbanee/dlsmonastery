package net.myspring.common.mybatis;

import net.myspring.mybatis.form.BaseForm;
import net.myspring.mybatis.mapper.BaseMapper;
import net.myspring.mybatis.mapper.CrudMapper;
import net.myspring.mybatis.provider.CrudProvider;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuj on 2017/3/27.
 */
public interface MyMapper<T, ID extends Serializable> extends BaseMapper<T, ID> {

    @UpdateProvider(type=MyProvider.class,method = "logicDeleteOne")
    int logicDeleteOne(ID id);

    @UpdateProvider(type=MyProvider.class,method = "logicDeleteByIds")
    int logicDeleteByIds(List<ID> ids);

    @SelectProvider(type = MyProvider.class,method = "findAllEnabled")
    List<T> findAllEnabled();

    @InsertProvider(type = MyProvider.class, method = "save")
    @Options(useGeneratedKeys = true)
    <S extends T> int save(S var1);

    @InsertProvider(type = MyProvider.class, method = "batchSave")
    @Options(useGeneratedKeys = true)
    <S extends T> int batchSave(Iterable<S> var1);

    @SelectProvider(type = MyProvider.class, method = "findOne")
    T findOne(ID var1);

    @SelectProvider(type = MyProvider.class, method = "exists")
    Boolean exists(ID var1);

    @SelectProvider(type = MyProvider.class, method = "findAll")
    List<T> findAll();

    @SelectProvider(type = MyProvider.class, method = "findByIds")
    List<T> findByIds(Iterable<ID> var1);

    @SelectProvider(type = MyProvider.class, method = "count")
    Long count();

    @UpdateProvider(type = MyProvider.class, method = "update")
    <S extends T> int update(S var1);

    @UpdateProvider(type = MyProvider.class, method = "update")
    <S extends BaseForm<T>> int updateForm(S var1);

}
