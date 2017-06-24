package net.myspring.future.modules.layout.web.query;

import com.google.common.collect.Lists;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.collection.CollectionUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by wangzm on 2017/5/11.
 */
public class ShopAttributeQuery extends BaseQuery {

    private String shopName;
    private String createdBy;
    private LocalDate createdDate;

}
