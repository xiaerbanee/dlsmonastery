package net.myspring.common.query;

import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by liuj on 2017/4/6.
 */
public class PageableQuery {
    private Pageable pageable;
    private BaseQuery query;

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public BaseQuery getQuery() {
        return query;
    }

    public void setQuery(BaseQuery query) {
        this.query = query;
    }
}
