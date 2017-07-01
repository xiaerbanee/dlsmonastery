package net.myspring.tool.common.utils;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.GlobalToolApplication;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.ToolClient;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import net.myspring.tool.modules.oppo.service.OppoService;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service

@Transactional
public class ScheduleUtils {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CompanyConfigClient companyConfigClient;
	@Autowired
	private ToolClient toolClient;

	@Scheduled(cron = "*/20 * * * * ?")
	public void synOppo() {
		logger.info("工厂自动同步开始");
		String date= LocalDateUtils.format(LocalDate.now());
		String message=toolClient.synFactoryOppo(date);
		logger.info(message+"工厂自动同步成功");
	}
}
