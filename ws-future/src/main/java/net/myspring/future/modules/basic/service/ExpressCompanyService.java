package net.myspring.future.modules.basic.service;

import net.myspring.future.common.enums.ExpressCompanyTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.basic.repository.ExpressCompanyRepository;
import net.myspring.future.modules.basic.web.form.ExpressCompanyForm;
import net.myspring.future.modules.basic.web.query.ExpressCompanyQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
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
    private CacheUtils cacheUtils;


    public ExpressCompanyDto findOne(String id){
        ExpressCompanyDto expressCompanyDto = new ExpressCompanyDto();
        if(StringUtils.isNotBlank(id)){
            ExpressCompany expressCompany=expressCompanyRepository.findOne(id);
            expressCompanyDto = BeanUtil.map(expressCompany,ExpressCompanyDto.class);
            cacheUtils.initCacheInput(expressCompanyDto);
        }
        return expressCompanyDto;
    }

    public ExpressCompanyForm getForm(ExpressCompanyForm expressCompanyForm){
        expressCompanyForm.setExpressTypeList(ExpressCompanyTypeEnum.getList());
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
        expressCompanyRepository.logicDelete(expressCompanyForm.getId());
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

    public ExpressCompanyQuery getQuery(ExpressCompanyQuery expressCompanyQuery) {
        expressCompanyQuery.setExpressTypeList(ExpressCompanyTypeEnum.getList());
        return expressCompanyQuery;
    }

    public List<ExpressCompanyDto> findByNameLike(String name) {
        List<ExpressCompany> expressCompanies = expressCompanyRepository.findByNameContaining(name);
        return BeanUtil.map(expressCompanies,ExpressCompanyDto.class);
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

}
