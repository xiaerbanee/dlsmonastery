package net.myspring.basic;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.sys.mapper.DictMapMapper;
import net.myspring.util.time.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private CacheUtils cacheUtils;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LocalDateTime start = LocalDateTime.now();
        logger.info("init cache start at " + LocalDateTimeUtils.format(start,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        cacheUtils.initCache("account",accountMapper.findAll());
        cacheUtils.initCache("dictMap",dictMapMapper.findAll());
        LocalDateTime end = LocalDateTime.now();
        logger.info("init cache end at " + LocalDateTimeUtils.format(end,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        logger.info("init cache in " + ChronoUnit.MILLIS.between(start, end) + " mills");
    }
}
