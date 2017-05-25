package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import net.myspring.tool.modules.oppo.mapper.*;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by guolm on 2017/5/9.
 */
@Service
@LocalDataSource
public class OppoService {
    @Autowired
    private OppoMapper oppoMapper;
    @Autowired
    private OppoPlantProductSelMapper oppoPlantProductSelMapper;
    @Autowired
    private OppoPlantAgentProductSelMapper oppoPlantAgentProductSelMapper;
    @Autowired
    private OppoPlantSendImeiPpselMapper oppoPlantSendImeiPpselMapper;
    @Autowired
    private OppoPlantProductItemelectronSelMapper oppoPlantProductItemelectronSelMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @FactoryDataSource
    public List<OppoPlantProductSel> plantProductSel(String companyId, String password, String branchId) {
        return oppoMapper.plantProductSel(companyId, password, branchId);
    }

    @FactoryDataSource
    public List<OppoPlantAgentProductSel> plantAgentProductSel(String companyId, String password, String branchId) {
        return oppoMapper.plantAgentProductSel(companyId, password, branchId);
    }

    @FactoryDataSource
    public List<OppoPlantSendImeiPpsel> plantSendImeiPPSel(String companyId, String password, LocalDate createdTime) {
        return oppoMapper.plantSendImeiPPSel(companyId, password, createdTime);
    }

    @FactoryDataSource
    public List<OppoPlantProductItemelectronSel> plantProductItemelectronSel(String companyId, String password, LocalDate systemDate) {
        return oppoMapper.plantProductItemelectronSel(companyId, password, systemDate);
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
            List<String> localColorIds = oppoPlantProductSelMapper.findColorIds(colorIds);
            for (OppoPlantProductSel oppoPlantProductSel : oppoPlantProductSels) {
                if (!localColorIds.contains(oppoPlantProductSel.getColorId().trim())) {
                    list.add(oppoPlantProductSel);
                }
            }
            if (CollectionUtil.isNotEmpty(list)) {
                oppoPlantProductSelMapper.save(list);
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
            List<String> localItemNumbers = oppoPlantAgentProductSelMapper.findItemNumbers(itemNumbers);
            for (OppoPlantAgentProductSel oppoPlantAgentProductSel : oppoPlantAgentProductSels) {
                if (!localItemNumbers.contains(oppoPlantAgentProductSel.getItemNumber())) {
                    list.add(oppoPlantAgentProductSel);
                }
            }
            if (CollectionUtil.isNotEmpty(list)) {
                oppoPlantAgentProductSelMapper.save(list);
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
        List<String> localImeis = oppoPlantSendImeiPpselMapper.findImeis(imeis);
        for (OppoPlantSendImeiPpsel oppoPlantSendImeiPpsel : oppoPlantSendImeiPpsels) {
            if (!localImeis.contains(oppoPlantSendImeiPpsel.getImei())) {
                oppoPlantSendImeiPpsel.setCompanyId(agentCode);
                list.add(oppoPlantSendImeiPpsel);
            }
        }
        if (CollectionUtil.isNotEmpty(list)) {
            oppoPlantSendImeiPpselMapper.save(list);
        }
        return "发货串码同步成功，共"+list.size()+"条";
    }

    // 获取电子保卡信息
    public String pullPlantProductItemelectronSels(List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels) {
        List<OppoPlantProductItemelectronSel> list = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(oppoPlantProductItemelectronSels)) {
            List<String> productNos = CollectionUtil.extractToList(oppoPlantProductItemelectronSels, "productNo");
            List<String> localProductNos = oppoPlantProductItemelectronSelMapper.findProductNos(productNos);
            for (OppoPlantProductItemelectronSel oppoPlantProductItemelectronSel : oppoPlantProductItemelectronSels) {
                if (!localProductNos.contains(oppoPlantProductItemelectronSel.getProductNo())) {
                    list.add(oppoPlantProductItemelectronSel);
                }
            }
            if (CollectionUtil.isNotEmpty(list)) {
                oppoPlantProductItemelectronSelMapper.save(list);
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
        List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels = oppoPlantSendImeiPpselMapper.findSynList(dateStart, dateEnd, mainCodes);
        return oppoPlantSendImeiPpsels;
    }
}
