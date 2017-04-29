package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.CompanyConfig;
import net.myspring.basic.modules.sys.dto.CompanyConfigDto;
import net.myspring.basic.modules.sys.manager.CompanyConfigManager;
import net.myspring.basic.modules.sys.mapper.CompanyConfigMapper;
import net.myspring.basic.modules.sys.web.form.CompanyConfigForm;
import net.myspring.basic.modules.sys.web.query.CompanyConfigQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Created by zhucc on 2017/4/17.
 */
@Service
public class CompanyConfigService {

    @Autowired
    private CompanyConfigMapper companyConfigMapper;
    @Autowired
    private CompanyConfigManager companyConfigManager;

    @Autowired
    private CacheUtils cacheUtils;

   public Page<CompanyConfigDto> findPage(Pageable pageable,CompanyConfigQuery companyConfigQuery){
       Page<CompanyConfigDto> companyConfigDtoPage = companyConfigMapper.findPage(pageable,companyConfigQuery);
       cacheUtils.initCacheInput(companyConfigDtoPage.getContent());
       return companyConfigDtoPage;
   }

   public CompanyConfigForm save(CompanyConfigForm companyConfigForm){
       if(companyConfigForm.isCreate()){
           CompanyConfig companyConfig= BeanUtil.map(companyConfigForm,CompanyConfig.class);
           companyConfigManager.save(companyConfig);
       }else{
           companyConfigManager.updateForm(companyConfigForm);
       }
       return companyConfigForm;
   }

    public CompanyConfigDto findForm(String id){
        CompanyConfig companyConfig= companyConfigManager.findOne(id);
        CompanyConfigDto companyConfigDto = BeanUtil.map(companyConfig, CompanyConfigDto.class);
        cacheUtils.initCacheInput(companyConfigDto);
        return companyConfigDto;
    }

    public String getValueByCode(String code){
        CompanyConfig companyConfig=companyConfigMapper.findByCode(code);
        if(companyConfig!=null){
            return companyConfig.getValue();
        }
        return "";
    }
}
