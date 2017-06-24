package net.myspring.tool.common.utils;
import net.myspring.common.constant.CharConstant;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.service.OppoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Lazy(false)
public class ScheduleUtils {
//	protected Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private OppoService oppoService;

	@Transactional
	public String synOppo(LocalDate date) {
		return null;
		//List<String> mainCodes = Arrays.asList(oppoService.getCodes("FACTORY_AGENT_CODES").split(CharConstant.COMMA));
		//List<String> mainPasswords = Arrays.asList(oppoService.getCodes("FACTORY_AGENT_PASSWORD").split(CharConstant.COMMA));
		//同步颜色编码
		//List<OppoPlantProductSel> plantProductSel = oppoService.plantProductSel(mainCodes.get(0), mainPasswords.get(0), "");
		//String message=oppoService.pullPlantProductSels(plantProductSel);
		//return message;
//		//同步物料编码
//		List<OppoPlantAgentProductSel> oppoPlantAgentProductSels = oppoService.plantAgentProductSel(mainCodes.get(0), mainPasswords.get(0), "");
//		futureOppoService.pullPlantAgentProductSels(oppoPlantAgentProductSels);
//		//同步发货串吗
//		for (int i = 0; i < mainCodes.size(); i++) {
//			List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpselList = oppoService.plantSendImeiPPSel(mainCodes.get(i), mainPasswords.get(i), date);
//			if (Collections3.isNotEmpty(oppoPlantSendImeiPpselList)) {
//				futureOppoService.pullPlantSendImeiPpsels(oppoPlantSendImeiPpselList, mainCodes.get(i));
//			}
//		}
//		//同步电子保卡
//		List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels = oppoService.plantProductItemelectronSel(mainCodes.get(0), mainPasswords.get(0),date);
//		futureOppoService.pullPlantProductItemelectronSels(oppoPlantProductItemelectronSels);
//		//同步串吗到本地
//		return futureOppoService.syn(date);
//	}
//
//	@Transactional
//	public String synVivo(LocalDate date) {
//		FutureVivoService futureVivoService = ToolsApplication.getApplicationContext().getBean(FutureVivoService.class);
//		VivoService vivoService = ToolsApplication.getApplicationContext().getBean(VivoService.class);
//		futureVivoService.setAccount("1");
//		//同步颜色编码
//		String companyName = ThreadLocalContext.get().getCompanyName();
//		if(!"IDVIVO".equals(companyName)){
//			List<VivoProducts> vivoProductsList = vivoService.products();
//			futureVivoService.pullProducts(vivoProductsList);
//		}
//		//同步物料编码
//		List<VivoPlantProducts> vivoPlantProductsList = vivoService.plantProducts();
//		futureVivoService.pullPlantProducts(vivoPlantProductsList);
//		//同步发货串吗
//		List<String> agentCodes =  Arrays.asList(futureVivoService.getCodes("FACTORY_AGENT_CODES").split(CharConstant.COMMA));
//		List<VivoPlantSendimei> vivoPlantSendimeis = vivoService.plantSendimei(date,agentCodes);
//		futureVivoService.pullPlantSendimeis(vivoPlantSendimeis);
//		//同步电子保卡
//		List<VivoPlantElectronicsn> vivoPlantElectronicsns = vivoService.plantElectronicsn(date);
//		futureVivoService.pullPlantElectronicsns(vivoPlantElectronicsns);
//		//同步串吗到本地
//		return futureVivoService.syn(date);
//	}
//
//	@Transactional
//	public String synImoo(LocalDate date){
//		FutureImooService futureImooService = ToolsApplication.getApplicationContext().getBean(FutureImooService.class);
//		ImooService imooService = ToolsApplication.getApplicationContext().getBean(ImooService.class);
//
//		List<ImooPlantBasicProduct> imooPlantBasicProducts = imooService.imooPlantBasicProducts();
//		futureImooService.pullPlantProducts(imooPlantBasicProducts);
//		List<String> agentCodes = Arrays.asList(futureImooService.getCodes("FACTORY_AGENT_CODES").split(CharConstant.COMMA));
//		List<ImooPrdocutImeiDeliver> imooPrdocutImeiDelivers = imooService.plantPrdocutImeiDeliverByDate(agentCodes);
//		futureImooService.pullPlantProducts(imooPlantBasicProducts);
//		futureImooService.pullPlantSendimeis(imooPrdocutImeiDelivers);
//		if(date==null){
//			date=LocalDate.now();
//		}
//		return futureImooService.syn(date);
	}
}
