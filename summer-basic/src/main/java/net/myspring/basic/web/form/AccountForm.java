package net.myspring.basic.web.form;


import net.myspring.basic.modules.hr.domain.Account;

import javax.persistence.Column;

/**
 * Created by liuj on 2017/3/19.
 */
public class AccountForm extends Account {
    @Column(updatable = true,insertable = false)
    private String remarks;
}