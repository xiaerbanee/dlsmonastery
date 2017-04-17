package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.CompanyConfig;
import net.myspring.basic.modules.sys.mapper.CompanyConfigMapper;
import net.myspring.basic.modules.sys.web.form.CompanyConfigForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


/**
 * Created by zhucc on 2017/4/17.
 */
@Component
public class CompanyConfigManager {

    @Autowired
    private CompanyConfigMapper companyConfigMapper;

    @Cacheable(value = "companyConfigs",key="#p0.id")
    public CompanyConfig save(CompanyConfig companyConfig){
        companyConfigMapper.save(companyConfig);
        return  companyConfig;
    }

    @CachePut(value = "companyConfigs",key="#p0.id")
    public CompanyConfig updateForm(CompanyConfigForm companyConfigForm){
        companyConfigMapper.updateForm(companyConfigForm);
        return  companyConfigMapper.findOne(companyConfigForm.getId());
    }

    @Cacheable(value = "dictMaps",key="#p0")
    public CompanyConfig findOne(String id) {
        return companyConfigMapper.findOne(id);
    }
}
