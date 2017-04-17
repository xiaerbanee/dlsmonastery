package net.myspring.future.common.query;

/**
 * Created by liuj on 2017/4/17.
 */
public class PageQuery<T> extends BaseQuery<T> {
    private Integer page = 0;
    private Integer size=25;
    private String sort = "id,DESC";

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
