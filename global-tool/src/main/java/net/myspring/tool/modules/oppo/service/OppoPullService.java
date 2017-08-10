package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.dto.OppoPlantSendImeiPpselDto;
import net.myspring.tool.modules.oppo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by guolm on 2017/5/9.
 */
@Service
@LocalDataSource
@Transactional(readOnly = true)
public class OppoPullService {
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
    public List<OppoPlantProductSel> getOppoPlantProductSels(String agentCode,String passWord){
        return oppoPlantProductSelRepository.plantProductSel(agentCode, passWord, "");
    }

    @FactoryDataSource
    public List<OppoPlantAgentProductSel> getOppoPlantAgentProductSels(String agentCode,String passWord){
        return oppoPlantAgentProductSelRepository.plantAgentProductSel(agentCode,passWord, "");
    }

    @FactoryDataSource
    public List<OppoPlantSendImeiPpsel> getOppoPlantSendImeiPpsels(String agentCode,String passWord,String date){
        return  oppoPlantSendImeiPpselRepository.plantSendImeiPPSel(agentCode, passWord, date);
    }

    @FactoryDataSource
    public List<OppoPlantProductItemelectronSel> getOppoPlantProductItemelectronSels(String agentCode,String passWord,String date){
        return  oppoPlantProductItemelectronSelRepository.plantProductItemelectronSel(agentCode,passWord, LocalDateUtils.format(LocalDateUtils.parse(date).minusDays(1)));
    }


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
    @Transactional
    public void pullPlantProductSels(List<OppoPlantProductSel> oppoPlantProductSels) {
        if (StringUtils.isBlank(DbContextHolder.get().getCompanyName())){
            logger.info("未设置公司名称"+ LocalDateTime.now());
            return;
        }
        String companyName = DbContextHolder.get().getCompanyName();
        logger.info("开始同步颜色编码");
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
            List<OppoPlantProductSel> plantProductSels =oppoPlantProductSelRepository.findColorIds(colorIds,companyName);
            if(CollectionUtil.isNotEmpty(plantProductSels)){
                localColorIds=CollectionUtil.extractToList(plantProductSels,"colorId");
            }
            for (OppoPlantProductSel oppoPlantProductSel : oppoPlantProductSels) {
                if (CollectionUtil.isEmpty(localColorIds)||!localColorIds.contains(oppoPlantProductSel.getColorId().trim())) {
                    oppoPlantProductSel.setColorId(oppoPlantProductSel.getColorId().trim());
                    oppoPlantProductSel.setCompanyName(companyName);
                    list.add(oppoPlantProductSel);
                }
            }
            if (CollectionUtil.isNotEmpty(list)) {
                oppoPlantProductSelRepository.save(list);
            }
            logger.info("颜色编码同步成功");
        }
    }

    //获取物料编码
    @LocalDataSource
    @Transactional
    public void pullPlantAgentProductSels(List<OppoPlantAgentProductSel> oppoPlantAgentProductSels) {
        String companyName = DbContextHolder.get().getCompanyName();
        if (StringUtils.isBlank(companyName)){
            logger.info("未设置公司名称"+LocalDateTime.now());
            return;
        }
        logger.info("开始物料编码");
        List<OppoPlantAgentProductSel> list = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(oppoPlantAgentProductSels)) {
            List<String> itemNumbers = CollectionUtil.extractToList(oppoPlantAgentProductSels, "itemNumber");
            List<String> localItemNumbers=Lists.newArrayList();
            List<OppoPlantAgentProductSel> plantAgentProductSels=Lists.newArrayList();
            for(List<String> stringList:CollectionUtil.splitList(itemNumbers,500)){
                plantAgentProductSels.addAll(oppoPlantAgentProductSelRepository.findItemNumbers(stringList,companyName));
            }
            if(CollectionUtil.isNotEmpty(plantAgentProductSels)){
                localItemNumbers=CollectionUtil.extractToList(plantAgentProductSels, "itemNumber");
            }
            for (OppoPlantAgentProductSel oppoPlantAgentProductSel : oppoPlantAgentProductSels) {
                if (!localItemNumbers.contains(oppoPlantAgentProductSel.getItemNumber())) {
                    oppoPlantAgentProductSel.setCompanyName(companyName);
                    list.add(oppoPlantAgentProductSel);
                }
            }
            if (CollectionUtil.isNotEmpty(list)) {
                oppoPlantAgentProductSelRepository.save(list);
            }
            logger.info("物料编码同步成功");
        }
    }

    //获取发货串码信息
    @LocalDataSource
    @Transactional
    public void pullPlantSendImeiPpsels(Map<String,List<OppoPlantSendImeiPpsel>> oppoPlantSendImeiPpselMap) {
        String companyName = DbContextHolder.get().getCompanyName();
        if (StringUtils.isBlank(companyName)){
            logger.info("未设置的当前公司"+LocalDateTime.now());
            return;
        }
        logger.info("发货串码开始同步");
        List<String> imeiList=Lists.newArrayList();
        for (String agentCode : oppoPlantSendImeiPpselMap.keySet()) {
            List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels=oppoPlantSendImeiPpselMap.get(agentCode);
            if (CollectionUtil.isNotEmpty(oppoPlantSendImeiPpsels)) {
                for(OppoPlantSendImeiPpsel plantSendImeiPpsel:oppoPlantSendImeiPpsels){
                    plantSendImeiPpsel.setCompanyId(agentCode);
                    imeiList.add(plantSendImeiPpsel.getImei());
                }
            }
        }
        List<OppoPlantSendImeiPpsel> localPlantSendImeiPpsels=Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(imeiList)){
            for(List<String> imeis:CollectionUtil.splitList(imeiList,1000)){
                localPlantSendImeiPpsels.addAll(oppoPlantSendImeiPpselRepository.findByimeis(imeis,companyName));
            }
        }
        List<String> localImeis=Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(localPlantSendImeiPpsels)){
            localImeis=CollectionUtil.extractToList(localPlantSendImeiPpsels, "imei");
        }
        List<OppoPlantSendImeiPpsel> saveOppoPlantSendImeiPpselList=Lists.newArrayList();
        for (String agentCode : oppoPlantSendImeiPpselMap.keySet()) {
            List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels=oppoPlantSendImeiPpselMap.get(agentCode);
            if (CollectionUtil.isNotEmpty(oppoPlantSendImeiPpsels)) {
                for(OppoPlantSendImeiPpsel plantSendImeiPpsel:oppoPlantSendImeiPpsels){
                   if(!localImeis.contains(plantSendImeiPpsel.getImei())){
                       plantSendImeiPpsel.setCompanyName(companyName);
                       saveOppoPlantSendImeiPpselList.add(plantSendImeiPpsel);
                   }
                }
            }
        }
        if (CollectionUtil.isNotEmpty(saveOppoPlantSendImeiPpselList)) {
            oppoPlantSendImeiPpselRepository.save(saveOppoPlantSendImeiPpselList);
        }
        logger.info("发货串码同步成功");
    }

    // 获取电子保卡信息
    @LocalDataSource
    @Transactional
    public void pullPlantProductItemelectronSels(List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels) {
        String companyName = DbContextHolder.get().getCompanyName();
        if (StringUtils.isBlank(companyName)){
            logger.info("未设置当前公司名称"+LocalDateTime.now());
            return;
        }
        logger.info("开始同步电子保卡");
        List<OppoPlantProductItemelectronSel> list = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(oppoPlantProductItemelectronSels)) {
            List<String> productNoList = CollectionUtil.extractToList(oppoPlantProductItemelectronSels, "productNo");
            List<String> localProductNos=Lists.newArrayList();
            List<OppoPlantProductItemelectronSel> plantProductItemelectronSels=Lists.newArrayList();
            if(CollectionUtil.isNotEmpty(productNoList)){
                for(List<String> productNos:CollectionUtil.splitList(productNoList,1000)){
                    plantProductItemelectronSels.addAll(oppoPlantProductItemelectronSelRepository.findProductNos(productNos,companyName));
                }
            }
            if(CollectionUtil.isNotEmpty(plantProductItemelectronSels)){
                localProductNos=CollectionUtil.extractToList(plantProductItemelectronSels, "productNo");
            }
            for (OppoPlantProductItemelectronSel oppoPlantProductItemelectronSel : oppoPlantProductItemelectronSels) {
                if (!localProductNos.contains(oppoPlantProductItemelectronSel.getProductNo())) {
                    oppoPlantProductItemelectronSel.setProductNo(oppoPlantProductItemelectronSel.getProductNo().trim());
                    oppoPlantProductItemelectronSel.setCompanyName(companyName);
                    list.add(oppoPlantProductItemelectronSel);
                }
            }
            if (CollectionUtil.isNotEmpty(list)) {
                oppoPlantProductItemelectronSelRepository.save(list);
            }
            logger.info("电子保卡同步成功");
        }
    }

    @LocalDataSource
    public  List<OppoPlantSendImeiPpselDto>  getSendImeList(String date, String agentCode) {
        String dateStart =date;
        String dateEnd =LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        String companyName = DbContextHolder.get().getCompanyName();
        List<String>  mainCodes= StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        List<OppoPlantSendImeiPpselDto> oppoPlantSendImeiPpselDtos = oppoPlantSendImeiPpselRepository.findSynList(dateStart, dateEnd, mainCodes, companyName);
        return oppoPlantSendImeiPpselDtos;
    }

    @LocalDataSource
    public  List<OppoPlantProductItemelectronSel>  getItemelectronSelList(String date,String agentCode) {
        String dateStart =date;
        String dateEnd =LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<String>  mainCodes= StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        String companyName = DbContextHolder.get().getCompanyName();
        List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels = oppoPlantProductItemelectronSelRepository.findSynList(dateStart, dateEnd, mainCodes, companyName);
        return oppoPlantProductItemelectronSels;
    }
 }
