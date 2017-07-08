package net.myspring.tool.modules.oppo.utils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.ToolClient;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.domain.DistrictEntity;
import net.myspring.tool.common.domain.OfficeEntity;
import net.myspring.tool.common.dto.CustomerDto;
import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.repository.OppoPlantAgentProductSelRepository;
import net.myspring.tool.modules.oppo.repository.OppoPlantProductItemelectronSelRepository;
import net.myspring.tool.modules.oppo.repository.OppoPlantProductSelRepository;
import net.myspring.tool.modules.oppo.repository.OppoPlantSendImeiPpselRepository;
import net.myspring.tool.modules.oppo.web.controller.OppoController;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
@Service
public class OppoPullScheduleUtils {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private OppoController oppoController;

	@Transactional(readOnly = false)
	public void syn() {
		logger.info("工厂自动同步开始");
		oppoController.pullFactoryData(	LocalDateUtils.format(LocalDate.now()));
		logger.info("工厂自动同步成功");
	}
}
