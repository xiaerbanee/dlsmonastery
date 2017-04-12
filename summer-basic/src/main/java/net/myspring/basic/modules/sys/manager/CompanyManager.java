package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.Company;
import net.myspring.basic.modules.sys.mapper.CompanyMapper;
import net.myspring.basic.modules.sys.web.form.CompanyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangzm on 2017/4/12.
 */
@Component
public class CompanyManager {
    
    @Autowired
    private CompanyMapper companyMapper;

    @Cacheable(value = "companys",key="#p0")
    public Company findOne(String id){
        return companyMapper.findOne(id);
    }

    @Cacheable(value = "companys",key="#p0.id")
    public Company save(Company company){
        companyMapper.save(company);
        return  company;
    }

    @Cacheable(value = "companys",key="#p0.id")
    public Company saveForm(CompanyForm companyForm){
        companyMapper.saveForm(companyForm);
        return  companyMapper.findOne(companyForm.getId());
    }

    @CachePut(value = "companys",key="#p0.id")
    public Company update(Company company){
        companyMapper.update(company);
        return  companyMapper.findOne(company.getId());
    }

    @CachePut(value = "companys",key="#p0.id")
    public Company updateForm(CompanyForm companyForm){
        companyMapper.updateForm(companyForm);
        return  companyMapper.findOne(companyForm.getId());
    }

    @CacheEvict(value = "companys",key="#p0")
    public int deleteById(String id) {
        return companyMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
