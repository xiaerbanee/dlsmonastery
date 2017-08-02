package net.myspring.basic.modules.salary.web.form;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.salary.domain.SalaryTemplate;
import net.myspring.common.form.BaseForm;

import java.util.List;

public class SalaryTemplateForm extends BaseForm<SalaryTemplate>{
    private String name;
    private List<SalaryTemplateDetailForm> salaryTemplateDetailList= Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SalaryTemplateDetailForm> getSalaryTemplateDetailList() {
        return salaryTemplateDetailList;
    }

    public void setSalaryTemplateDetailList(List<SalaryTemplateDetailForm> salaryTemplateDetailList) {
        this.salaryTemplateDetailList = salaryTemplateDetailList;
    }
}
