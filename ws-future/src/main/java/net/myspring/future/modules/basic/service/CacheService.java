package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.repository.*;
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
    private ChainRepository chainRepository;
    @Autowired
    private PricesystemRepository pricesystemRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ExpressCompanyRepository expressCompanyRepository;
    @Autowired
    private AdpricesystemRepository adpricesystemRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public void init() {
        LocalDateTime start = LocalDateTime.now();
        logger.info("init cache start at " + LocalDateTimeUtils.format(start,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        cacheUtils.initCache("chains",chainRepository.findAll());
        cacheUtils.initCache("pricesystems",pricesystemRepository.findAll());
        cacheUtils.initCache("depots",depotRepository.findAll());
        cacheUtils.initCache("expressCompanys",expressCompanyRepository.findAll());
        cacheUtils.initCache("adPricesystem",adpricesystemRepository.findAll());
        LocalDateTime end = LocalDateTime.now();
        logger.info("init cache end at " + LocalDateTimeUtils.format(end,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        logger.info("init cache in " + ChronoUnit.MILLIS.between(start, end) + " mills");
    }
}
