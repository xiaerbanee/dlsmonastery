package net.myspring.tool.common.utils;
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
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private OppoPlantProductSelRepository oppoPlantProductSelRepository;
	@Autowired
	private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;
	@Autowired
	private OppoPlantSendImeiPpselRepository oppoPlantSendImeiPpselRepository;
	@Autowired
	private OppoPlantProductItemelectronSelRepository oppoPlantProductItemelectronSelRepository;


	public void syn() {
		logger.info("工厂自动同步开始");
		String date= LocalDateUtils.format(LocalDate.now());
		synOppo(date);
		logger.info("工厂自动同步成功");
	}

	@FactoryDataSource
	private void synOppo(String date){
		String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
		String[] agentCodes=agentCode.split(CharConstant.COMMA);
		String passWord=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_PASSWORDS.name()).replace("\"","");
		String[] passWords=passWord.split(CharConstant.COMMA);
		//获取颜色编码
		List<OppoPlantProductSel> oppoPlantProductSels=oppoPlantProductSelRepository.plantProductSel(agentCodes[0], passWords[0], "");
		//获取物料编码
		List<OppoPlantAgentProductSel> oppoPlantAgentProductSels = oppoPlantAgentProductSelRepository.plantAgentProductSel(agentCodes[0], passWords[0], "");
		//获取同步串码
		List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpselList= Lists.newArrayList();
		for (int i = 0; i < agentCodes.length; i++) {
			List<OppoPlantSendImeiPpsel> plantSendImeiPpsels = oppoPlantSendImeiPpselRepository.plantSendImeiPPSel(agentCodes[i], passWords[i], date);
			oppoPlantSendImeiPpselList.addAll(plantSendImeiPpsels);
		}
		//获取电子保卡
		List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels = oppoPlantProductItemelectronSelRepository.plantProductItemelectronSel(agentCodes[0],passWords[0], date);
		logger.info("oppoPlantProductSels=="+oppoPlantProductSels.toString());
		logger.info("oppoPlantAgentProductSels=="+oppoPlantAgentProductSels.toString());
		logger.info("oppoPlantSendImeiPpselList=="+oppoPlantSendImeiPpselList.toString());
		logger.info("oppoPlantProductItemelectronSels=="+oppoPlantProductItemelectronSels.toString());
	}
}
