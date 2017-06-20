package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
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
    private OppoRepository oppoRepository;
    @Autowired
    private OppoPlantProductSelRepository oppoPlantProductSelRepository;
    @Autowired
    private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;
    @Autowired
    private OppoPlantSendImeiPpselRepository oppoPlantSendImeiPpselRepository;
    @Autowired
    private OppoPlantProductItemelectronSelRepository oppoPlantProductItemelectronSelRepository;

    @FactoryDataSource
    public List<OppoPlantProductSel> plantProductSel(String companyId, String password, String branchId) {
        return oppoRepository.plantProductSel(companyId, password, branchId);
    }

    @FactoryDataSource
    public List<OppoPlantAgentProductSel> plantAgentProductSel(String companyId, String password, String branchId) {
        return oppoRepository.plantAgentProductSel(companyId, password, branchId);
    }

    @FactoryDataSource
    public List<OppoPlantSendImeiPpsel> plantSendImeiPPSel(String companyId, String password, LocalDate createdTime) {
        return oppoRepository.plantSendImeiPPSel(companyId, password, createdTime);
    }

    @FactoryDataSource
    public List<OppoPlantProductItemelectronSel> plantProductItemelectronSel(String companyId, String password, LocalDate systemDate) {
        return oppoRepository.plantProductItemelectronSel(companyId, password, systemDate);
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
                colorIds.add(oppoPlantProductSel.getColorId().trim());
            }
            List<String> localColorIds = oppoPlantProductSelRepository.findColorIds(colorIds);
            for (OppoPlantProductSel oppoPlantProductSel : oppoPlantProductSels) {
                if (!localColorIds.contains(oppoPlantProductSel.getColorId().trim())) {
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
            List<String> localItemNumbers = oppoPlantAgentProductSelRepository.findItemNumbers(itemNumbers);
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
        List<String> localImeis = oppoPlantSendImeiPpselRepository.findImeis(imeis);
        for (OppoPlantSendImeiPpsel oppoPlantSendImeiPpsel : oppoPlantSendImeiPpsels) {
            if (!localImeis.contains(oppoPlantSendImeiPpsel.getImei())) {
                oppoPlantSendImeiPpsel.setCompanyId(agentCode);
                list.add(oppoPlantSendImeiPpsel);
            }
        }
        if (CollectionUtil.isNotEmpty(list)) {
            oppoPlantSendImeiPpselRepository.save(list);
        }
        return "发货串码同步成功，共"+list.size()+"条";
    }

    // 获取电子保卡信息
    public String pullPlantProductItemelectronSels(List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels) {
        List<OppoPlantProductItemelectronSel> list = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(oppoPlantProductItemelectronSels)) {
            List<String> productNos = CollectionUtil.extractToList(oppoPlantProductItemelectronSels, "productNo");
            List<String> localProductNos = oppoPlantProductItemelectronSelRepository.findProductNos(productNos);
            for (OppoPlantProductItemelectronSel oppoPlantProductItemelectronSel : oppoPlantProductItemelectronSels) {
                if (!localProductNos.contains(oppoPlantProductItemelectronSel.getProductNo())) {
                    list.add(oppoPlantProductItemelectronSel);
                }
            }
            if (CollectionUtil.isNotEmpty(list)) {
                oppoPlantProductItemelectronSelRepository.save(list);
            }
        }
        return "电子保卡同步成功，共"+list.size()+"条";
    }



    @Transactional
    public  List<OppoPlantSendImeiPpsel>  synIme(String date) {
        LocalDate nowDate= LocalDateUtils.parse(date);
        LocalDate dateStart = nowDate.minusDays(1);
        LocalDate dateEnd = nowDate.plusDays(1);
       List<String>  mainCodes=Lists.newArrayList();
        mainCodes.add("M13AMB");
        List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels = oppoPlantSendImeiPpselRepository.findSynList(dateStart, dateEnd, mainCodes);
        return oppoPlantSendImeiPpsels;
    }
 }
