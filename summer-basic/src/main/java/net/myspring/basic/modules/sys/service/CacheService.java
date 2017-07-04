package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.repository.*;
import net.myspring.basic.modules.sys.domain.CompanyConfig;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.basic.modules.sys.dto.CompanyConfigDto;
import net.myspring.basic.modules.sys.repository.*;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.time.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by liuj on 2017/4/22.
 */
@Service
@Transactional(readOnly = true)
public class CacheService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DictMapRepository dictMapRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private OfficeRuleRepository officeRuleRepository;
    @Autowired
    private DictEnumRepository dictEnumRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuCategoryRepository menuCategoryRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private BackendRepository backendRepository;
    @Autowired
    private BackendModuleRepository backendModuleRepository;
    @Autowired
    private RoleModuleRepository positionBackendRepository;
    @Autowired
    private CompanyConfigRepository companyConfigRepository;

    public void init() {
        LocalDateTime start = LocalDateTime.now();
        logger.info("init cache start at " + LocalDateTimeUtils.format(start,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        cacheUtils.initCache("accounts",accountRepository.findAll());
        cacheUtils.initCache("dictMaps",dictMapRepository.findAll());
        cacheUtils.initCache("employees",employeeRepository.findAll());
        cacheUtils.initCache("offices",officeRepository.findAll());
        cacheUtils.initCache("dictEnums",dictEnumRepository.findAll());
        cacheUtils.initCache("positions",positionRepository.findAll());
        cacheUtils.initCache("menus",menuRepository.findAll());
        cacheUtils.initCache("menuCategorys",menuCategoryRepository.findAll());
        cacheUtils.initCache("backends",backendRepository.findAll());
        cacheUtils.initCache("backendModules",backendModuleRepository.findAll());
        cacheUtils.initCache("positionModules",positionBackendRepository.findAll());
        cacheUtils.initCache("officeRules",officeRuleRepository.findAll());
        List<CompanyConfigCacheDto> companyConfigs=companyConfigRepository.findAllCache();
        for(CompanyConfigCacheDto companyConfigCacheDto:companyConfigs) {
            companyConfigCacheDto.setKey(companyConfigCacheDto.getCode());
        }
        cacheUtils.initCache("companyConfigCodes",companyConfigs,"key");
        LocalDateTime end = LocalDateTime.now();
        logger.info("init cache end at " + LocalDateTimeUtils.format(end,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        logger.info("init cache in " + ChronoUnit.MILLIS.between(start, end) + " mills");
    }
}
