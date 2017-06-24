package net.myspring.general.modules.sys.service;

import net.myspring.general.common.utils.CacheUtils;
import net.myspring.general.modules.sys.repository.DistrictRepository;
import net.myspring.util.time.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Component
public class CacheService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public void init() {
        LocalDateTime start = LocalDateTime.now();
        logger.info("init cache start at " + LocalDateTimeUtils.format(start,LocalDateTimeUtils.FORMATTER_MILLISECOND));

        cacheUtils.initCache("districts", districtRepository.findAll());

        LocalDateTime end = LocalDateTime.now();
        logger.info("init cache end at " + LocalDateTimeUtils.format(end,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        logger.info("init cache in " + ChronoUnit.MILLIS.between(start, end) + " mills");
    }
}
