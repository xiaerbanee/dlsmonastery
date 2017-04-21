package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.mapper.ExpressCompanyMapper;
import net.myspring.future.modules.basic.web.form.ExpressCompanyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
public class ExpressCompanyManager {
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;

    @Cacheable(value = "expressCompanies",key="#p0")
    public ExpressCompany findOne(String id) {
        return expressCompanyMapper.findOne(id);
    }

    @Cacheable(value = "expressCompanys",key="#p0.id")
    public ExpressCompany save(ExpressCompany expressCompany){
        expressCompanyMapper.save(expressCompany);
        return  expressCompany;
    }

    @CachePut(value = "expressCompanys",key="#p0.id")
    public ExpressCompany update(ExpressCompany expressCompany){
        expressCompanyMapper.update(expressCompany);
        return  expressCompanyMapper.findOne(expressCompany.getId());
    }

    @CachePut(value = "expressCompanys",key="#p0.id")
    public ExpressCompany updateForm(ExpressCompanyForm expressCompanyForm){
        expressCompanyMapper.updateForm(expressCompanyForm);
        return  expressCompanyMapper.findOne(expressCompanyForm.getId());
    }

    @CacheEvict(value = "expressCompanys",key="#p0")
    public int deleteById(String id) {
        return expressCompanyMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
