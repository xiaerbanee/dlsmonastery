package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.mapper.*;
import net.myspring.basic.modules.sys.mapper.*;
import net.myspring.util.time.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by liuj on 2017/4/22.
 */
@Component
public class CacheService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private DictMapMapper dictMapMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private OfficeRuleMapper officeRuleMapper;
    @Autowired
    private DictEnumMapper dictEnumMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuCategoryMapper menuCategoryMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private BackendMapper backendMapper;
    @Autowired
    private BackendModuleMapper backendModuleMapper;
    @Autowired
    private RoleModuleMapper positionBackendMapper;

    public void init() {
        LocalDateTime start = LocalDateTime.now();
        logger.info("init cache start at " + LocalDateTimeUtils.format(start,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        cacheUtils.initCache("accounts",accountMapper.findAll());
        cacheUtils.initCache("dictMaps",dictMapMapper.findAll());
        cacheUtils.initCache("employees",employeeMapper.findAll());
        cacheUtils.initCache("offices",officeMapper.findAll());
        cacheUtils.initCache("dictEnums",dictEnumMapper.findAll());
        cacheUtils.initCache("positions",positionMapper.findAll());
        cacheUtils.initCache("menus",menuMapper.findAll());
        cacheUtils.initCache("companys",companyMapper.findAll());
        cacheUtils.initCache("menuCategorys",menuCategoryMapper.findAll());
        cacheUtils.initCache("backends",backendMapper.findAll());
        cacheUtils.initCache("backendModules",backendModuleMapper.findAll());
        cacheUtils.initCache("positionModules",positionBackendMapper.findAll());
        LocalDateTime end = LocalDateTime.now();
        logger.info("init cache end at " + LocalDateTimeUtils.format(end,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        logger.info("init cache in " + ChronoUnit.MILLIS.between(start, end) + " mills");
    }
}
