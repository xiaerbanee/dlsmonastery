package net.myspring.tool.common.utils;
import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.ToolClient;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.enums.CompanyNameEnum;
import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import net.myspring.tool.modules.oppo.repository.OppoPlantAgentProductSelRepository;
import net.myspring.tool.modules.oppo.repository.OppoPlantProductItemelectronSelRepository;
import net.myspring.tool.modules.oppo.repository.OppoPlantProductSelRepository;
import net.myspring.tool.modules.oppo.repository.OppoPlantSendImeiPpselRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = false)
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

	@Transactional(readOnly = true)
	@Scheduled(cron = "*/5 * * * * ?")
	public void syn() {
		logger.info("工厂自动同步开始");
		String date= LocalDateUtils.format(LocalDate.now());
		synOppo(date);
		logger.info("工厂自动同步成功");
	}

	@Transactional(readOnly = false)
	public void synOppo(String date){
		String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
		DbContextHolder.get().setDataSourceType(DataSourceTypeEnum.FACTORY.name());
		DbContextHolder.get().setCompanyName(companyName);
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
		List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels = oppoPlantProductItemelectronSelRepository.plantProductItemelectronSel(agentCodes[0],passWords[0], LocalDateUtils.format(LocalDateUtils.parse(date).minusDays(1)));
		DbContextHolder.get().setDataSourceType(DataSourceTypeEnum.LOCAL.name());
		DbContextHolder.get().setCompanyName(companyName);
		//同步颜色编码
		pullPlantProductSels(oppoPlantProductSels);
		//同步物料编码
		pullPlantAgentProductSels(oppoPlantAgentProductSels);
		//同步串码
		for (int i = 0; i < agentCodes.length; i++) {
			if (CollectionUtil.isNotEmpty(oppoPlantSendImeiPpselList)) {
				pullPlantSendImeiPpsels(oppoPlantSendImeiPpselList, agentCodes[i]);
			}
		}
//		同步电子保卡
		pullPlantProductItemelectronSels(oppoPlantProductItemelectronSels);
	}

	@LocalDataSource
	@Transactional(readOnly = false)
	void pullPlantProductSels(List<OppoPlantProductSel> oppoPlantProductSels) {
		for(OppoPlantProductSel oppoPlantProductSel:oppoPlantProductSels){
			oppoPlantProductSel.setColorId(oppoPlantProductSel.getColorId().trim());
		}
		List<OppoPlantProductSel> list = Lists.newArrayList();
		if (CollectionUtil.isNotEmpty(oppoPlantProductSels)) {
			List<String> colorIds = Lists.newArrayList();
			for (OppoPlantProductSel oppoPlantProductSel : oppoPlantProductSels) {
				if(!colorIds.contains(oppoPlantProductSel.getColorId().trim())){
					colorIds.add(oppoPlantProductSel.getColorId().trim());
				}
			}
			List<String> localColorIds=Lists.newArrayList();
			List<OppoPlantProductSel> plantProductSels =oppoPlantProductSelRepository.findColorIds(colorIds);
			if(CollectionUtil.isNotEmpty(plantProductSels)){
				localColorIds=CollectionUtil.extractToList(plantProductSels,"colorId");
			}
			for (OppoPlantProductSel oppoPlantProductSel : oppoPlantProductSels) {
				if (CollectionUtil.isEmpty(localColorIds)||!localColorIds.contains(oppoPlantProductSel.getColorId().trim())) {
					oppoPlantProductSel.setColorId(oppoPlantProductSel.getColorId().trim());
					list.add(oppoPlantProductSel);
				}
			}
			if (CollectionUtil.isNotEmpty(list)) {
				oppoPlantProductSelRepository.save(list);
			}
		}
	}

	//获取物料编码
	@LocalDataSource
	@Transactional(readOnly = false)
	void pullPlantAgentProductSels(List<OppoPlantAgentProductSel> oppoPlantAgentProductSels) {
		List<OppoPlantAgentProductSel> list = Lists.newArrayList();
		if (CollectionUtil.isNotEmpty(oppoPlantAgentProductSels)) {
			List<String> itemNumbers = CollectionUtil.extractToList(oppoPlantAgentProductSels, "itemNumber");
			List<String> localItemNumbers=Lists.newArrayList();
			List<OppoPlantAgentProductSel> plantAgentProductSels=Lists.newArrayList();
			for(List<String> stringList:CollectionUtil.splitList(itemNumbers,500)){
				plantAgentProductSels.addAll(oppoPlantAgentProductSelRepository.findItemNumbers(stringList));
			}
			if(CollectionUtil.isNotEmpty(plantAgentProductSels)){
				localItemNumbers=CollectionUtil.extractToList(plantAgentProductSels, "itemNumber");
			}
			for (OppoPlantAgentProductSel oppoPlantAgentProductSel : oppoPlantAgentProductSels) {
				if (!localItemNumbers.contains(oppoPlantAgentProductSel.getItemNumber())) {
					list.add(oppoPlantAgentProductSel);
				}
			}
			if (CollectionUtil.isNotEmpty(list)) {
				oppoPlantAgentProductSelRepository.save(list);
			}
		}
	}

	//获取发货串码信息
	@LocalDataSource
	@Transactional(readOnly = false)
	void pullPlantSendImeiPpsels(List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels, String agentCode) {
		List<OppoPlantSendImeiPpsel> list = Lists.newArrayList();
		List<String> imeiList = CollectionUtil.extractToList(oppoPlantSendImeiPpsels, "imei");
		List<OppoPlantSendImeiPpsel> plantSendImeiPpsels=Lists.newArrayList();
		for(List<String> imeis:CollectionUtil.splitList(imeiList,1000)){
			plantSendImeiPpsels.addAll(oppoPlantSendImeiPpselRepository.findByimeis(imeis));
		}
		List<String> localImeis=Lists.newArrayList();
		if(CollectionUtil.isNotEmpty(plantSendImeiPpsels)){
			localImeis=CollectionUtil.extractToList(plantSendImeiPpsels, "imei");
		}
		for (OppoPlantSendImeiPpsel oppoPlantSendImeiPpsel : oppoPlantSendImeiPpsels) {
			if (CollectionUtil.isEmpty(localImeis)||!localImeis.contains(oppoPlantSendImeiPpsel.getImei())) {
				oppoPlantSendImeiPpsel.setCompanyId(agentCode);
				list.add(oppoPlantSendImeiPpsel);
			}
		}
		if (CollectionUtil.isNotEmpty(list)) {
			oppoPlantSendImeiPpselRepository.batchSave(list);
		}
	}

	// 获取电子保卡信息
	@LocalDataSource
	@Transactional(readOnly = false)
	void pullPlantProductItemelectronSels(List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels) {
		List<OppoPlantProductItemelectronSel> list = Lists.newArrayList();
		if (CollectionUtil.isNotEmpty(oppoPlantProductItemelectronSels)) {
			List<String> productNoList = CollectionUtil.extractToList(oppoPlantProductItemelectronSels, "productNo");
			List<String> localProductNos=Lists.newArrayList();
			List<OppoPlantProductItemelectronSel> plantProductItemelectronSels=Lists.newArrayList();
			for(List<String> productNos:CollectionUtil.splitList(productNoList,1000)){
				plantProductItemelectronSels.addAll(oppoPlantProductItemelectronSelRepository.findProductNos(productNos));
			}
			if(CollectionUtil.isNotEmpty(plantProductItemelectronSels)){
				localProductNos=CollectionUtil.extractToList(plantProductItemelectronSels, "productNo");
			}
			for (OppoPlantProductItemelectronSel oppoPlantProductItemelectronSel : oppoPlantProductItemelectronSels) {
				if (!localProductNos.contains(oppoPlantProductItemelectronSel.getProductNo())) {
					oppoPlantProductItemelectronSel.setProductNo(oppoPlantProductItemelectronSel.getProductNo().trim());
					list.add(oppoPlantProductItemelectronSel);
				}
			}
			logger.info("开始同步电子保卡");
			if (CollectionUtil.isNotEmpty(list)) {
				oppoPlantProductItemelectronSelRepository.batchSave(list);
			}
			logger.info("电子保卡同步成功");
		}
	}
}
