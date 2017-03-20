package net.myspring.mybatis.mapper;

import net.myspring.mybatis.provider.CrudProvider;
import net.myspring.mybatis.provider.PagingAndSortingProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * Created by liuj on 2017/3/18.
 */
public interface PagingAndSortingMapper<T, ID extends Serializable> extends CrudMapper<T, ID> {
    /**
     * Returns all entities sorted by the given options.
     *
     * @param sort
     * @return all entities sorted by the given options
     */
    @SelectProvider(type=PagingAndSortingProvider.class, method = "findAllBySort")
    Iterable<T> findAllBySort(Sort sort);

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable
     * @return a page of entities
     */
    @SelectProvider(type=PagingAndSortingProvider.class, method = "findAllByPageable")
    Page<T> findAllByPageable(Pageable pageable);
}
