package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.discovery.converters.Auto;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.Const;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.domain.VivoProducts;
import net.myspring.tool.modules.vivo.mapper.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by guolm on 2017/5/12.
 */
@Service
@LocalDataSource
public class VivoService {
    @Autowired
    private VivoMapper vivoMapper;
    @Autowired
    private VivoProductsMapper vivoProductsMapper;
    @Autowired
    private VivoPlantProductsMapper vivoPlantProductsMapper;
    @Autowired
    private VivoPlantSendimeiMapper vivoPlantSendimeiMapper;
    @Autowired
    private VivoPlantElectronicsnMapper vivoPlantElectronicsnMapper;

    @FactoryDataSource
    public List<VivoProducts> products() {
        return vivoMapper.products();
    }

    @FactoryDataSource
    public List<VivoPlantProducts> plantProducts() {
        return vivoMapper.plantProducts();
    }

    @FactoryDataSource
    public List<VivoPlantSendimei> plantSendimei(LocalDate createdTime, List<String> agentCodes) {
        LocalDate dateStart = createdTime;
        LocalDate dateEnd = createdTime.plusDays(1);
        return vivoMapper.plantSendimei(dateStart, dateEnd, agentCodes);
    }

    @FactoryDataSource
    public List<VivoPlantElectronicsn> plantElectronicsn(LocalDate retailDate) {
        LocalDate dateStart = retailDate;
        LocalDate dateEnd = retailDate.plusDays(1);
        return vivoMapper.plantElectronicsn(dateStart, dateEnd);
    }

    public String getCodes(String type) {
        return Const.CompanyConfig.getMap().get("JXVIVO" + Const.CharEnum.UNDER_LINE.getValue() + type);
    }


    //获取颜色编码
    @LocalDataSource
    @Transactional(readOnly = false)
    public void pullProducts(List<VivoProducts> vivoProducts){
        if(CollectionUtil.isNotEmpty(vivoProducts)) {
            List<String> colorIds = CollectionUtil.extractToList(vivoProducts, "colorId");
            List<String > localColorIds = vivoProductsMapper.findColorIds(colorIds);
            List<VivoProducts> list = Lists.newArrayList();
            for(VivoProducts item : vivoProducts){
                if( ! localColorIds.contains(item.getColorId())){
                    list.add(item);
                }
            }
            if(CollectionUtil.isNotEmpty(list)){
                vivoProductsMapper.batchSave(list);
            }
        }
    }
    //获取物料编码
    @Transactional(readOnly = false)
    public void pullPlantProducts( List<VivoPlantProducts> vivoPlantProducts){
        if(CollectionUtil.isNotEmpty(vivoPlantProducts)) {
            List<String> itemNumbers =CollectionUtil.extractToList(vivoPlantProducts, "itemNumber");
            List<String> newItemNumbers=Lists.newArrayList();
            for(String itemNumber:itemNumbers){
                newItemNumbers.add(itemNumber.trim());
            }
            List<String> localItemNumbers = vivoPlantProductsMapper.findItemNumbers(newItemNumbers);
            List<VivoPlantProducts> list= Lists.newArrayList();
            for(VivoPlantProducts item : vivoPlantProducts){
                if(!localItemNumbers.contains(item.getItemNumber().trim())){
                    list.add(item);
                }
            }
            if(CollectionUtil.isNotEmpty(list)) {
                vivoPlantProductsMapper.save(list);
            }
        }
    }

    //查询发货串码
    @LocalDataSource
    @Transactional(readOnly = false)
    public String pullPlantSendimeis(List<VivoPlantSendimei> vivoPlantSendimeis){
        List<VivoPlantSendimei> list = Lists.newArrayList();
        Map<String,List<VivoPlantSendimei>> agentCodeMap= Maps.newHashMap();
        for(VivoPlantSendimei vivoPlantSendimei:vivoPlantSendimeis){
            if(!agentCodeMap.containsKey(vivoPlantSendimei.getCompanyId())){
                List<VivoPlantSendimei> plantSendimeis=Lists.newArrayList();
                agentCodeMap.put(vivoPlantSendimei.getCompanyId(),plantSendimeis);
            }
            agentCodeMap.get(vivoPlantSendimei.getCompanyId()).add(vivoPlantSendimei);
        }
        Map<String,VivoPlantSendimei> map = CollectionUtil.extractToMap(vivoPlantSendimeis,"imei");
        Set<String> imeis=map.keySet();
        for(List<String> imeiList:Lists.partition(new ArrayList<String>(imeis),1500)){
            Set<String> localImeis = vivoPlantSendimeiMapper.findImeis(imeiList);
            for(String imei:imeiList){
                if(!localImeis.contains(imei)){
                    list.add(map.get(imei));
                }
            }
        }
        if(CollectionUtil.isNotEmpty(list)){
            for(List<VivoPlantSendimei> vivoPlantSendimeisList :Lists.partition(list,1500)) {
                vivoPlantSendimeiMapper.save(vivoPlantSendimeisList);
            }
        }
        return "发货串码同步成功，共同步"+list.size()+"条数据";
    }


    //查询电子保卡
    @LocalDataSource
    @Transactional(readOnly = false)
    public String pullPlantElectronicsns(List<VivoPlantElectronicsn> vivoPlantElectronicsns) {
        List<VivoPlantElectronicsn> list = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(vivoPlantElectronicsns)) {
            List<String> snImeis = CollectionUtil.extractToList(vivoPlantElectronicsns, "snImei");
            List<String> localsnImeis  = vivoPlantElectronicsnMapper.findSnImeis(snImeis);
            for(VivoPlantElectronicsn item : vivoPlantElectronicsns){
                if( ! localsnImeis.contains(item.getSnImei())){
                    list.add(item);
                }
            }
            if(CollectionUtil.isNotEmpty(list)) {
                vivoPlantElectronicsnMapper.save(list);
            }
        }
        return "电子保卡同步成功,共同步"+list.size()+"条数据";
    }
}
