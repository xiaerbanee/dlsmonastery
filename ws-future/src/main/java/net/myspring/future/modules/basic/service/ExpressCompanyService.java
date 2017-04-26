package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.DistrictClient;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.basic.manager.ExpressCompanyManager;
import net.myspring.future.modules.basic.mapper.ExpressCompanyMapper;
import net.myspring.future.modules.basic.web.query.ExpressCompanyQuery;
import net.myspring.future.modules.basic.web.form.ExpressCompanyForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExpressCompanyService {

    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private ExpressCompanyManager expressCompanyManager;
    @Autowired
    private DistrictClient districtClient;
    @Autowired
    private CacheUtils cacheUtils;


    public ExpressCompany findOne(String id){
        ExpressCompany expressCompany=expressCompanyManager.findOne(id);
        return expressCompany;
    }

    public ExpressCompanyForm findForm(ExpressCompanyForm expressCompanyForm){
        if(!expressCompanyForm.isCreate()){
            ExpressCompany expressCompany=expressCompanyManager.findOne(expressCompanyForm.getId());
            expressCompanyForm=BeanUtil.map(expressCompany,ExpressCompanyForm.class);
            cacheUtils.initCacheInput(expressCompanyForm);
        }
        return expressCompanyForm;
    }

    public List<ExpressCompany> findByExpressType(String type){
        List<ExpressCompany> expressCompanyList=expressCompanyMapper.findByExpressType(type);
        return expressCompanyList;
    }

    public List<ExpressCompany> findAll(){
        return expressCompanyMapper.findAll();
    }

    public Page<ExpressCompanyDto> findPage(Pageable pageable, ExpressCompanyQuery expressCompanyQuery) {
        Page<ExpressCompanyDto> page = expressCompanyMapper.findPage(pageable, expressCompanyQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(ExpressCompanyForm expressCompanyForm) {
        expressCompanyMapper.logicDeleteOne(expressCompanyForm.getId());
    }

    public ExpressCompany save(ExpressCompanyForm expressCompanyForm){
        ExpressCompany expressCompany;
        if(expressCompanyForm.isCreate()){
            expressCompany= BeanUtil.map(expressCompanyForm,ExpressCompany.class);
            expressCompany=expressCompanyManager.save(expressCompany);
        }else{
            expressCompany=expressCompanyManager.updateForm(expressCompanyForm);
        }
        return expressCompany;
    }
}
