package net.myspring.future.modules.basic.service;

import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.ExpressCompanyTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.basic.repository.ExpressCompanyRepository;
import net.myspring.future.modules.basic.web.form.ExpressCompanyForm;
import net.myspring.future.modules.basic.web.query.ExpressCompanyQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private RedisTemplate redisTemplate;

    public ExpressCompanyDto findDto(String id){
        ExpressCompanyDto expressCompanyDto=new ExpressCompanyDto();
        if(StringUtils.isNotBlank(id)) {
             expressCompanyDto = BeanUtil.map(expressCompanyRepository.findOne(id), ExpressCompanyDto.class);
            cacheUtils.initCacheInput(expressCompanyDto);
        }
        return expressCompanyDto;
    }

    public ExpressCompanyForm getForm(ExpressCompanyForm expressCompanyForm){
        if(!expressCompanyForm.isCreate()){
            ExpressCompany expressCompany=expressCompanyRepository.findOne(expressCompanyForm.getId());
            expressCompanyForm=BeanUtil.map(expressCompany,ExpressCompanyForm.class);
            cacheUtils.initCacheInput(expressCompanyForm);
        }
        return expressCompanyForm;
    }

    public List<ExpressCompanyDto> findAll(){
        List<ExpressCompany> expressCompanyList =  expressCompanyRepository.findAll();
        return BeanUtil.map(expressCompanyList,ExpressCompanyDto.class);
    }

    public Page<ExpressCompanyDto> findPage(Pageable pageable, ExpressCompanyQuery expressCompanyQuery) {
        Page<ExpressCompanyDto> page = expressCompanyRepository.findPage(pageable, expressCompanyQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(String id) {
        expressCompanyRepository.logicDelete(id);
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

    public List<ExpressCompanyDto> findByNameLike(String name) {
        List<ExpressCompanyDto> result = expressCompanyRepository.findByNameLike(RequestUtils.getCompanyId(), name);
        cacheUtils.initCacheInput(result);
        return result;
    }

    public String getDefaultExpressCompanyId() {
        String defaultExpressCompanyId = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.DEFAULT_EXPRESS_COMPANY_ID.name()).getValue();
        return StringUtils.trimToNull(defaultExpressCompanyId);
    }

}
