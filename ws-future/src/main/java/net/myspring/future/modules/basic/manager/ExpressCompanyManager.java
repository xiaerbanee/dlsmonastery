package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.mapper.ExpressCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

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
}
