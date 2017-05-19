package net.myspring.future.modules.basic.service;

import net.myspring.future.common.enums.ExpressCompanyTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.basic.mapper.ExpressCompanyMapper;
import net.myspring.future.modules.basic.web.form.ExpressCompanyForm;
import net.myspring.future.modules.basic.web.query.ExpressCompanyQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
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
    private CacheUtils cacheUtils;


    public ExpressCompany findOne(String id){
        ExpressCompany expressCompany=expressCompanyMapper.findOne(id);
        return expressCompany;
    }

    public ExpressCompanyForm getFormProperty(ExpressCompanyForm expressCompanyForm){
        if(!expressCompanyForm.isCreate()){
            ExpressCompany expressCompany=expressCompanyMapper.findOne(expressCompanyForm.getId());
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
            expressCompanyMapper.save(expressCompany);
        }else{
            expressCompany=expressCompanyMapper.findOne(expressCompanyForm.getId());
            ReflectionUtil.copyProperties(expressCompanyForm,expressCompany);
            expressCompanyMapper.update(expressCompany);
        }
        return expressCompany;
    }

    public List<String> findExpressTypeList() {
        return ExpressCompanyTypeEnum.getList();
    }

    public List<ExpressCompanyDto> findByNameLike(String name) {
        return expressCompanyMapper.findByNameLike(RequestUtils.getCompanyId(), name);
    }

    public String getDefaultExpressCompanyId() {
        //TODO default expressCompanyID
//        String code = Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfig.CompanyConfigCode.DEFAULT_EXPRESS_COMPANY_ID.getCode());
//        if (StringUtils.isNotBlank(code)) {
//            ExpressCompany expressCompany = expressCompanyDao.findOne(Long.valueOf(code));
//            storeAllotForm.setExpressCompanyId( expressCompanyService.getDefaultExpressCompanyId());
//        }

        return null;
    }

    public List<ExpressCompanyDto> findDtoListByCompanyIdAndExpressType(String companyId, String expressType) {
        return expressCompanyMapper.findDtoListByCompanyIdAndExpressType(companyId, expressType);
    }
}
