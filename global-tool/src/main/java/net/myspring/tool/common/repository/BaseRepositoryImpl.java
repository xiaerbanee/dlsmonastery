package net.myspring.tool.common.repository;

import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    private final EntityManager entityManager;

    private JpaEntityInformation<T, ?> entityInformation;


    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public Map<ID,T> findMap(Collection<ID> ids) {
        Map<ID,T> map = new HashMap<>();
        if(CollectionUtil.isNotEmpty(ids)) {
            List<T> list = findAll(ids);
            if(CollectionUtil.isNotEmpty(list)) {
                for(T t:list) {
                    ID id = (ID) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(t);
                    map.put(id,t);
                }
            }
        }
        return map;
    }

    @Override
    public T logicDeleteOne(ID id) {
        T t = null;
        if(id != null) {
            t = findOne(id);
            if(t != null) {
                ReflectionUtil.setFieldValue(t,"enabled",false);
                save(t);
            }
        }
        return t;
    }
}