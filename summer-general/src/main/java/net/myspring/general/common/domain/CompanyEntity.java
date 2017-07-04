package net.myspring.general.common.domain;

import net.myspring.general.common.utils.RequestUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.Date;

/**
 * Created by liuj on 2017/4/12.
 */
@MappedSuperclass
public class CompanyEntity<T> extends DataEntity<T> {
}
