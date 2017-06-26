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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by guolm on 2017/5/9.
 */
@Service
@LocalDataSource
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
    public List<OppoPlantProductItemelectronSel> plantProductItemelectronSel(String companyId, String password, LocalDate systemDate) {
        return  oppoPlantProductItemelectronSelRepository.plantProductItemelectronSel(companyId, password, LocalDateUtils.format(systemDate));
    }

    //获取颜色编码
    @LocalDataSource
    @Transactional(readOnly = false)
    public String pullPlantProductSels(List<OppoPlantProductSel> oppoPlantProductSels) {
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
        return "颜色编码同步成功，共" + list.size() + "条";
    }

    //获取物料编码
    @LocalDataSource
    @Transactional(readOnly = false)
    public String pullPlantAgentProductSels(List<OppoPlantAgentProductSel> oppoPlantAgentProductSels) {
        List<OppoPlantAgentProductSel> list = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(oppoPlantAgentProductSels)) {
            List<String> itemNumbers = CollectionUtil.extractToList(oppoPlantAgentProductSels, "itemNumber");
            List<String> localItemNumbers=Lists.newArrayList();
            List<OppoPlantAgentProductSel> plantAgentProductSels= oppoPlantAgentProductSelRepository.findItemNumbers(itemNumbers);
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
        return "物料编码同步成功，共"+list.size()+"条";
    }

    //获取发货串码信息
    @LocalDataSource
    @Transactional(readOnly = false)
    public String pullPlantSendImeiPpsels(List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels, String agentCode) {
        List<OppoPlantSendImeiPpsel> list = Lists.newArrayList();
        List<String> imeis = CollectionUtil.extractToList(oppoPlantSendImeiPpsels, "imei");
        List<OppoPlantSendImeiPpsel> plantSendImeiPpsels = oppoPlantSendImeiPpselRepository.findByimeis(imeis);
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
        logger.info("开始同步串码");
        if (CollectionUtil.isNotEmpty(list)) {
            oppoPlantSendImeiPpselRepository.save(list);
        }
        logger.info("串码同步成功");
        return "发货串码同步成功，共"+list.size()+"条";
    }

    // 获取电子保卡信息
    public String pullPlantProductItemelectronSels(List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels) {
        List<OppoPlantProductItemelectronSel> list = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(oppoPlantProductItemelectronSels)) {
            List<String> productNos = CollectionUtil.extractToList(oppoPlantProductItemelectronSels, "productNo");
            List<String> localProductNos=Lists.newArrayList();
            List<OppoPlantProductItemelectronSel> plantProductItemelectronSels = oppoPlantProductItemelectronSelRepository.findProductNos(productNos);
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
                oppoPlantProductItemelectronSelRepository.save(list);
            }
            logger.info("电子保卡同步成功");
        }
        return "电子保卡同步成功，共"+list.size()+"条";
    }



    @Transactional
    public  List<OppoPlantSendImeiPpselDto>  synIme(String date,String agentCode) {
        LocalDate nowDate= LocalDateUtils.parse(date);
        String dateStart =LocalDateUtils.format(LocalDateUtils.parse(date).minusDays(1));
        String dateEnd =LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<String>  mainCodes= StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        List<OppoPlantSendImeiPpselDto> oppoPlantSendImeiPpselDtos = oppoPlantSendImeiPpselRepository.findSynList(dateStart, dateEnd, mainCodes);
        return oppoPlantSendImeiPpselDtos;
    }
 }
