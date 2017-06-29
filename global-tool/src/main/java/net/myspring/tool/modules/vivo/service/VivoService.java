package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.domain.VivoProducts;
import net.myspring.tool.modules.vivo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
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
@Transactional
public class VivoService {
    @Autowired
    private VivoRepository vivoRepository;
    @Autowired
    private VivoProductsRepository vivoProductsRepository;
    @Autowired
    private VivoPlantProductsRepository vivoPlantProductsRepository;
    @Autowired
    private VivoPlantSendimeiRepository vivoPlantSendimeiRepository;
    @Autowired
    private VivoPlantElectronicsnRepository vivoPlantElectronicsnRepository;

    @FactoryDataSource
    public List<VivoProducts> products() {
        return vivoRepository.products();
    }

    @FactoryDataSource
    public List<VivoPlantProducts> plantProducts() {
        return vivoRepository.plantProducts();
    }

    @FactoryDataSource
    public List<VivoPlantSendimei> plantSendimei(LocalDate createdTime, List<String> agentCodes) {
        LocalDate dateStart = createdTime;
        LocalDate dateEnd = createdTime.plusDays(1);
        return vivoRepository.plantSendimei(dateStart, dateEnd, agentCodes);
    }

    @FactoryDataSource
    public List<VivoPlantElectronicsn> plantElectronicsn(LocalDate retailDate) {
        LocalDate dateStart = retailDate;
        LocalDate dateEnd = retailDate.plusDays(1);
        return vivoRepository.plantElectronicsn(dateStart, dateEnd);
    }

    //获取颜色编码
    @LocalDataSource
    public void pullProducts(List<VivoProducts> vivoProducts){
        if(CollectionUtil.isNotEmpty(vivoProducts)) {
            List<String> colorIds = CollectionUtil.extractToList(vivoProducts, "colorId");
            List<String > localColorIds = vivoProductsRepository.findColorIds(colorIds);
            List<VivoProducts> list = Lists.newArrayList();
            for(VivoProducts item : vivoProducts){
                if( ! localColorIds.contains(item.getColorId())){
                    list.add(item);
                }
            }
            if(CollectionUtil.isNotEmpty(list)){
                vivoProductsRepository.save(list);
            }
        }
    }
    //获取物料编码
    public void pullPlantProducts( List<VivoPlantProducts> vivoPlantProducts){
        if(CollectionUtil.isNotEmpty(vivoPlantProducts)) {
            List<String> itemNumbers =CollectionUtil.extractToList(vivoPlantProducts, "itemNumber");
            List<String> newItemNumbers=Lists.newArrayList();
            for(String itemNumber:itemNumbers){
                newItemNumbers.add(itemNumber.trim());
            }
            List<String> localItemNumbers = vivoPlantProductsRepository.findItemNumbers(newItemNumbers);
            List<VivoPlantProducts> list= Lists.newArrayList();
            for(VivoPlantProducts item : vivoPlantProducts){
                if(!localItemNumbers.contains(item.getItemNumber().trim())){
                    list.add(item);
                }
            }
            if(CollectionUtil.isNotEmpty(list)) {
                vivoPlantProductsRepository.save(list);
            }
        }
    }

    //查询发货串码
    @LocalDataSource
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
            Set<String> localImeis = vivoPlantSendimeiRepository.findImeis(imeiList);
            for(String imei:imeiList){
                if(!localImeis.contains(imei)){
                    list.add(map.get(imei));
                }
            }
        }
        if(CollectionUtil.isNotEmpty(list)){
            for(List<VivoPlantSendimei> vivoPlantSendimeisList :Lists.partition(list,1500)) {
                vivoPlantSendimeiRepository.save(vivoPlantSendimeisList);
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
            List<String> localsnImeis  = vivoPlantElectronicsnRepository.findSnImeis(snImeis);
            for(VivoPlantElectronicsn item : vivoPlantElectronicsns){
                if( ! localsnImeis.contains(item.getSnImei())){
                    list.add(item);
                }
            }
//            if(CollectionUtil.isNotEmpty(list)) {
//                vivoPlantElectronicsnRepository.save(list);
//            }
        }
        return "电子保卡同步成功,共同步"+list.size()+"条数据";
    }

    public  List<VivoPlantSendimei>  synIme(String date) {
        LocalDate nowDate= LocalDateUtils.parse(date);
        LocalDate dateStart = nowDate.minusDays(1);
        LocalDate dateEnd = nowDate.plusDays(1);
        List<String>  mainCodes=Lists.newArrayList();
        mainCodes.add("M13E00");
        List<VivoPlantSendimei> vivoPlantSendimeis = vivoPlantSendimeiRepository.findSynList(dateStart, dateEnd, mainCodes);
        return vivoPlantSendimeis;
    }
}
