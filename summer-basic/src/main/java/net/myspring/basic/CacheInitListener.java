package net.myspring.basic;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.mapper.*;
import net.myspring.basic.modules.sys.mapper.*;
import net.myspring.util.time.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by liuj on 2017/4/2.
 */
@Component
public class CacheInitListener implements ApplicationListener<ApplicationReadyEvent> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private DictMapMapper dictMapMapper;
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private DictEnumMapper dictEnumMapper;
    @Autowired
    private DistrictMapper districtMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuCategoryMapper menuCategoryMapper;
    @Autowired
    private TownMapper townMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LocalDateTime start = LocalDateTime.now();
        logger.info("init cache start at " + LocalDateTimeUtils.format(start,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        cacheUtils.initCache("accounts",accountMapper.findAll());
        cacheUtils.initCache("dictMaps",dictMapMapper.findAll());
        cacheUtils.initCache("jobs",jobMapper.findAll());
        cacheUtils.initCache("employees",employeeMapper.findAll());
        cacheUtils.initCache("offices",officeMapper.findAll());
        cacheUtils.initCache("dictEnums",dictEnumMapper.findAll());
        cacheUtils.initCache("districts",districtMapper.findAll());
        cacheUtils.initCache("positions",positionMapper.findAll());
        cacheUtils.initCache("towns",townMapper.findAll());
        cacheUtils.initCache("menus",menuMapper.findAll());
        cacheUtils.initCache("companys",companyMapper.findAll());
        cacheUtils.initCache("menuCategorys",menuCategoryMapper.findAll());
        LocalDateTime end = LocalDateTime.now();
        logger.info("init cache end at " + LocalDateTimeUtils.format(end,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        logger.info("init cache in " + ChronoUnit.MILLIS.between(start, end) + " mills");
    }
}
