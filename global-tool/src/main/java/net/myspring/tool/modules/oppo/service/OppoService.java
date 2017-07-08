package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guolm on 2017/5/9.
 */
@Service
@LocalDataSource
@Transactional(readOnly = false)
public class OppoService {
    @Autowired
    private OppoPlantProductSelRepository oppoPlantProductSelRepository;
    @Autowired
    private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;
    @Autowired
    private OppoPlantSendImeiPpselRepository oppoPlantSendImeiPpselRepository;
    @Autowired
    private OppoPlantProductItemelectronSelRepository oppoPlantProductItemelectronSelRepository;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @FactoryDataSource
    public List<OppoPlantProductSel>  plantProductSel(String companyId, String password, String branchId) {
        return oppoPlantProductSelRepository.plantProductSel(companyId, password, branchId);
    }

    @FactoryDataSource
    public List<OppoPlantAgentProductSel> plantAgentProductSel(String companyId, String password, String branchId) {
        return oppoPlantAgentProductSelRepository.plantAgentProductSel(companyId, password, branchId);
    }

    @FactoryDataSource
    public List<OppoPlantSendImeiPpsel> plantSendImeiPPSel(String companyId, String password, String dateTime) {
        return oppoPlantSendImeiPpselRepository.plantSendImeiPPSel(companyId, password, dateTime);
    }

    @FactoryDataSource
    public List<OppoPlantProductItemelectronSel> plantProductItemelectronSel(String companyId, String password, String date) {
        return  oppoPlantProductItemelectronSelRepository.plantProductItemelectronSel(companyId, password, date);
    }

    @LocalDataSource
    public  List<OppoPlantSendImeiPpselDto>  synIme(String date,String agentCode) {
        String dateStart =date;
        String dateEnd =LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<String>  mainCodes= StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        List<OppoPlantSendImeiPpselDto> oppoPlantSendImeiPpselDtos = oppoPlantSendImeiPpselRepository.findSynList(dateStart, dateEnd, mainCodes);
        return oppoPlantSendImeiPpselDtos;
    }

    @LocalDataSource
    public  List<OppoPlantProductItemelectronSel>  synProductItemelectronSel(String date,String agentCode) {
        String dateStart =LocalDateUtils.format(LocalDateUtils.parse(date).minusDays(1));
        String dateEnd =date;
        List<String>  mainCodes= StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels = oppoPlantProductItemelectronSelRepository.findSynList(dateStart, dateEnd, mainCodes);
        return oppoPlantProductItemelectronSels;
    }
 }
