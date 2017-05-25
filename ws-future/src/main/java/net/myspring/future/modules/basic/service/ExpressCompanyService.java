package net.myspring.future.modules.basic.service;

import net.myspring.future.common.enums.ExpressCompanyTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.basic.repository.ExpressCompanyRepository;
import net.myspring.future.modules.basic.repository.ExpressCompanyRepository;
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
    private ExpressCompanyRepository expressCompanyRepository;
    @Autowired
    private ExpressCompanyRepository expressCompanyRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public ExpressCompany findOne(String id){
        ExpressCompany expressCompany=expressCompanyRepository.findOne(id);
        return expressCompany;
    }

    public ExpressCompanyForm getForm(ExpressCompanyForm expressCompanyForm){
        if(!expressCompanyForm.isCreate()){
            ExpressCompany expressCompany=expressCompanyRepository.findOne(expressCompanyForm.getId());
            expressCompanyForm=BeanUtil.map(expressCompany,ExpressCompanyForm.class);
            cacheUtils.initCacheInput(expressCompanyForm);
        }
        return expressCompanyForm;
    }

    public List<ExpressCompany> findByExpressType(String type){
        List<ExpressCompany> expressCompanyList=expressCompanyRepository.findByExpressType(type);
        return expressCompanyList;
    }

    public List<ExpressCompany> findAll(){
        return expressCompanyRepository.findAll();

    }

    public Page<ExpressCompanyDto> findPage(Pageable pageable, ExpressCompanyQuery expressCompanyQuery) {
        Page<ExpressCompanyDto> page = expressCompanyRepository.findPage(pageable, expressCompanyQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(ExpressCompanyForm expressCompanyForm) {
        expressCompanyRepository.logicDeleteOne(expressCompanyForm.getId());
    }

    public ExpressCompany save(ExpressCompanyForm expressCompanyForm){
        ExpressCompany expressCompany;
        if(expressCompanyForm.isCreate()){
            expressCompany= BeanUtil.map(expressCompanyForm,ExpressCompany.class);
            expressCompanyRepository.save(expressCompany);
        }else{
            expressCompany=expressCompanyRepository.findOne(expressCompanyForm.getId());
            ReflectionUtil.copyProperties(expressCompanyForm,expressCompany);
            expressCompanyRepository.save(expressCompany);
        }
        return expressCompany;
    }

    public List<String> findExpressTypeList() {
        return ExpressCompanyTypeEnum.getList();
    }

    public List<ExpressCompanyDto> findByNameLike(String name) {
        return expressCompanyRepository.findByNameLike(RequestUtils.getCompanyId(), name);
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
        return expressCompanyRepository.findByCompanyIdAndExpressType(companyId, expressType);
    }
}
