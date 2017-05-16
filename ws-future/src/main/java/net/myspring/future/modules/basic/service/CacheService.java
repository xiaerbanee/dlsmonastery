package net.myspring.future.modules.basic.service;

import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.mapper.*;
import net.myspring.util.time.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by liuj on 2017/4/22.
 */
@Component
public class CacheService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ChainMapper chainMapper;
    @Autowired
    private PricesystemMapper pricesystemMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public void init() {
        LocalDateTime start = LocalDateTime.now();
        logger.info("init cache start at " + LocalDateTimeUtils.format(start,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        cacheUtils.initCache("chains",chainMapper.findAll());
        cacheUtils.initCache("pricesystems",pricesystemMapper.findAll());
        cacheUtils.initCache("depots",depotMapper.findAll());
        cacheUtils.initCache("expressCompanys",expressCompanyMapper.findAll());
        cacheUtils.initCache("adPricesystem",adPricesystemMapper.findAll());
        LocalDateTime end = LocalDateTime.now();
        logger.info("init cache end at " + LocalDateTimeUtils.format(end,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        logger.info("init cache in " + ChronoUnit.MILLIS.between(start, end) + " mills");
    }
}
