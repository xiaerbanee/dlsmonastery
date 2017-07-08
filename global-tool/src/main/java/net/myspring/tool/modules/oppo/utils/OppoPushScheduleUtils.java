package net.myspring.tool.modules.oppo.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.*;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.dataSource.DynamicDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.domain.DistrictEntity;
import net.myspring.tool.common.domain.EmployeeEntity;
import net.myspring.tool.common.domain.OfficeEntity;
import net.myspring.tool.common.dto.CustomerDto;
import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.repository.*;
import net.myspring.tool.modules.oppo.service.OppoPushSerivce;
import net.myspring.tool.modules.oppo.service.OppoService;
import net.myspring.tool.modules.oppo.web.controller.OppoController;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OppoPushScheduleUtils {

	@Autowired
	private OppoController oppoController;

//	@Scheduled(cron = "*/20* * * * ?")
	public void synFactory() {
		String date=LocalDateUtils.format(LocalDate.now());
		oppoController.pushFactoryData(date);
	}
}
