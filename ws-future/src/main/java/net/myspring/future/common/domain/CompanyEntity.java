package net.myspring.future.common.domain;


import net.myspring.future.common.utils.RequestUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

/**
 * Created by liuj on 2017/4/12.
 */
@MappedSuperclass
public class CompanyEntity<T> extends DataEntity<T> {

}
