package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import com.netflix.discovery.converters.Auto;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.Const;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.mapper.OppoMapper;
import net.myspring.tool.modules.oppo.mapper.OppoPlantAgentProductSelMapper;
import net.myspring.tool.modules.oppo.mapper.OppoPlantProductSelMapper;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    public String getCodes(String type) {
        return Const.CompanyConfig.getMap().get("JXOPPO" + Const.CharEnum.UNDER_LINE.getValue() + type);
    }

    @FactoryDataSource
    public List<OppoPlantProductSel> plantProductSel(String companyId, String password, String branchId) {
        return oppoMapper.plantProductSel(companyId, password, branchId);
    }

    @FactoryDataSource
    public List<OppoPlantAgentProductSel> plantAgentProductSel(String companyId, String password, String branchId) {
        return oppoMapper.plantAgentProductSel(companyId, password, branchId);
    }


    //获取颜色编码
    @LocalDataSource
    @Transactional(readOnly = false)
    public String pullPlantProductSels(List<OppoPlantProductSel> oppoPlantProductSels) {
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
}
