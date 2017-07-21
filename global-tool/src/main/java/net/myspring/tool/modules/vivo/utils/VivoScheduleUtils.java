package net.myspring.tool.modules.vivo.utils;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.ToolClient;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.modules.vivo.repository.*;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly=false)
public class VivoScheduleUtils {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CompanyConfigClient companyConfigClient;
	@Autowired
	private ToolClient toolClient;
	@Autowired
	private VivoProductsRepository vivoProductsRepository;
	@Autowired
	private VivoPlantProductsRepository vivoPlantProductsRepository;
	@Autowired
	private VivoPlantElectronicsnRepository vivoPlantElectronicsnRepository;

	public void syn() {
		logger.info("工厂自动同步开始");
		String date= LocalDateUtils.format(LocalDate.now());
		synVivo(date);
		logger.info("工厂自动同步成功");
	}

	@FactoryDataSource
	public void synVivo(String date){

	}
}
