package net.myspring.basic.common.mybatis;

import net.myspring.mybatis.form.BaseForm;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuj on 2017/3/27.
 */
public interface MyMapper<T, ID extends Serializable> extends BaseMapper<T, ID> {

    @UpdateProvider(type= MyProvider.class,method = MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(ID id);

    @UpdateProvider(type= MyProvider.class,method = MyProvider.LOGIC_DELETE_BY_IDS)
    int logicDeleteByIds(List<ID> ids);

    @SelectProvider(type = MyProvider.class,method = MyProvider.FIND_ALL_ENABLED)
    List<T> findAllEnabled();

    @InsertProvider(type = MyProvider.class, method = MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    <S extends T> int save(S var1);

    @InsertProvider(type = MyProvider.class, method = MyProvider.BATCH_SAVE)
    @Options(useGeneratedKeys = true)
    <S extends T> int batchSave(Iterable<S> var1);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    T findOne(ID var1);

    @SelectProvider(type = MyProvider.class, method = MyProvider.EXISTS)
    Boolean exists(ID var1);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<T> findAll();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_BY_IDS)
    List<T> findByIds(Iterable<ID> var1);

    @SelectProvider(type = MyProvider.class, method = MyProvider.COUNT)
    Long count();

    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    <S extends T> int update(S var1);

    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    <S extends BaseForm<T>> int updateForm(S var1);

}
