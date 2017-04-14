package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ExpressCompany;
import net.myspring.future.modules.crm.mapper.ExpressCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ExpressCompanyService {

    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;


    public ExpressCompany findOne(String id){
        ExpressCompany expressCompany=expressCompanyMapper.findOne(id);
        return expressCompany;
    }

    public List<ExpressCompany> findByExpressType(String type){
        List<ExpressCompany> expressCompanyList=expressCompanyMapper.findByExpressType(type);
        return expressCompanyList;
    }

    public List<ExpressCompany> findAll(){
        return expressCompanyMapper.findAll();
    }

    public Page<ExpressCompany> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ExpressCompany> page = expressCompanyMapper.findPage(pageable, map);
        return page;
    }

    public void delete(ExpressCompany expressCompany) {
        expressCompany.setEnabled(false);
        expressCompanyMapper.update(expressCompany);
    }

    @Transactional
    public void save(ExpressCompany expressCompany){
    }
}
