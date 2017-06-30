package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.CompanyConfig;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.basic.modules.sys.dto.CompanyConfigDto;
import net.myspring.basic.modules.sys.repository.CompanyConfigRepository;
import net.myspring.basic.modules.sys.web.form.CompanyConfigForm;
import net.myspring.basic.modules.sys.web.query.CompanyConfigQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by zhucc on 2017/4/17.
 */
@Service
@Transactional
public class CompanyConfigService {

    @Autowired
    private CompanyConfigRepository companyConfigRepository;

    @Autowired
    private CacheUtils cacheUtils;

    public Page<CompanyConfigDto> findPage(Pageable pageable, CompanyConfigQuery companyConfigQuery) {
        Page<CompanyConfigDto> companyConfigDtoPage = companyConfigRepository.findPage(pageable, companyConfigQuery);
        cacheUtils.initCacheInput(companyConfigDtoPage.getContent());
        return companyConfigDtoPage;
    }

    public CompanyConfigForm save(CompanyConfigForm companyConfigForm) {
        if (companyConfigForm.isCreate()) {
            CompanyConfig companyConfig = BeanUtil.map(companyConfigForm, CompanyConfig.class);
            companyConfigRepository.save(companyConfig);
        } else {
            CompanyConfig companyConfig = companyConfigRepository.findOne(companyConfigForm.getId());
            ReflectionUtil.copyProperties(companyConfigForm, companyConfig);
            companyConfigRepository.save(companyConfig);
        }
        return companyConfigForm;
    }

    public CompanyConfigDto findOne(CompanyConfigDto companyConfigDto) {
        if (!companyConfigDto.isCreate()) {
            CompanyConfig companyConfig = companyConfigRepository.findOne(companyConfigDto.getId());
            companyConfigDto = BeanUtil.map(companyConfig, CompanyConfigDto.class);
            cacheUtils.initCacheInput(companyConfigDto);
        }
        return companyConfigDto;
    }

    public String getValueByCode(String code) {
        CompanyConfig companyConfig = companyConfigRepository.findByCode(code);
        if (companyConfig != null) {
            return companyConfig.getValue();
        }
        return "";
    }

    public void logicDelete(String id) {
        companyConfigRepository.logicDelete(id);
    }
}
