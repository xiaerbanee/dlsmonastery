package net.myspring.tool.modules.vivo.utils;
import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.ToolClient;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import net.myspring.tool.modules.oppo.repository.OppoPlantAgentProductSelRepository;
import net.myspring.tool.modules.oppo.repository.OppoPlantProductItemelectronSelRepository;
import net.myspring.tool.modules.oppo.repository.OppoPlantProductSelRepository;
import net.myspring.tool.modules.oppo.repository.OppoPlantSendImeiPpselRepository;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.domain.VivoProducts;
import net.myspring.tool.modules.vivo.repository.*;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly=false)
public class VivoScheduleUtils {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CompanyConfigClient companyConfigClient;
	@Autowired
	private ToolClient toolClient;
	@Autowired
	private VivoRepository vivoRepository;
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
