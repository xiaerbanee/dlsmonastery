package net.myspring.mybatis.provider;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by liuj on 2017/3/18.
 */
public class PagingAndSortingProvider extends CrudProvider {

    public String findAllBySort(Sort sort) {
        return "";
    }

    public String findAllByPageable(Pageable pageable) {
        return "";
    }
}
