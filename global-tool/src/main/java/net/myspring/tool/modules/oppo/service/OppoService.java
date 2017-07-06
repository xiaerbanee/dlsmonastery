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

    //获取颜色编码
    @LocalDataSource
    public void pullPlantProductSels(List<OppoPlantProductSel> oppoPlantProductSels) {
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
    public void pullPlantAgentProductSels(List<OppoPlantAgentProductSel> oppoPlantAgentProductSels) {
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
    public void pullPlantSendImeiPpsels(List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels, String agentCode) {
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
    public void pullPlantProductItemelectronSels(List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels) {
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
