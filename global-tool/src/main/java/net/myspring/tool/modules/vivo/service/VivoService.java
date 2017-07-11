package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.domain.VivoProducts;
import net.myspring.tool.modules.vivo.dto.FactoryOrderDto;
import net.myspring.tool.modules.vivo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by guolm on 2017/5/12.
 */
@Service
@LocalDataSource
@Transactional(readOnly = false)
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
    public List<VivoProducts> getProducts() {
        return vivoProductsRepository.findProducts();
    }

    @FactoryDataSource
    public List<VivoPlantProducts> getPlantProducts() {
        return vivoPlantProductsRepository.findPlantProducts();
    }

    @FactoryDataSource
    public List<VivoPlantSendimei> getPlantSendimei(String date, List<String> agentCodes) {
        String dateStart = date;
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date));
        return vivoPlantSendimeiRepository.findPlantSendimei(dateStart, dateEnd, agentCodes);
    }

    @FactoryDataSource
    public List<VivoPlantElectronicsn> getPlantElectronicsn(LocalDate retailDate) {
        LocalDate dateStart = retailDate;
        LocalDate dateEnd = retailDate.plusDays(1);
        return vivoRepository.plantElectronicsn(dateStart, dateEnd);
    }

    //获取颜色编码
    @LocalDataSource
    public void pullVivoProducts(List<VivoProducts> vivoProducts){
        if(CollectionUtil.isNotEmpty(vivoProducts)) {
            for(VivoProducts vivoProduct:vivoProducts){
                vivoProduct.setColorId(vivoProduct.getColorId().trim());
            }
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
            for(VivoPlantProducts plantProduct:vivoPlantProducts){
                plantProduct.setItemNumber(plantProduct.getItemNumber().trim());
            }
            List<String> itemNumbers =CollectionUtil.extractToList(vivoPlantProducts, "itemNumber");
            List<String> localItemNumbers = vivoPlantProductsRepository.findItemNumbers(itemNumbers);
            List<VivoPlantProducts> list= Lists.newArrayList();
            for(VivoPlantProducts plantProduct : vivoPlantProducts){
                if(!localItemNumbers.contains(plantProduct.getItemNumber().trim())){
                    plantProduct.setItemNumber(plantProduct.getItemNumber().trim());
                    list.add(plantProduct);
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
        if(CollectionUtil.isNotEmpty(vivoPlantSendimeis)){
            List<String> imeiList = Lists.newArrayList();
            Map<String,List<VivoPlantSendimei>> agentCodeMap= Maps.newHashMap();
            for(VivoPlantSendimei vivoPlantSendimei:vivoPlantSendimeis){
                if(!agentCodeMap.containsKey(vivoPlantSendimei.getCompanyId())){
                    List<VivoPlantSendimei> plantSendimeis=Lists.newArrayList();
                    agentCodeMap.put(vivoPlantSendimei.getCompanyId(),plantSendimeis);
                }
                imeiList.add(vivoPlantSendimei.getImei());
                agentCodeMap.get(vivoPlantSendimei.getCompanyId()).add(vivoPlantSendimei);
            }
            List<String> localImeiList=vivoPlantSendimeiRepository.findImeis(imeiList);
            for(String agentCode:agentCodeMap.keySet()){
                List<VivoPlantSendimei>  plantSendimeis=agentCodeMap.get(agentCode);
            }
        }



        Map<String,VivoPlantSendimei> map = CollectionUtil.extractToMap(vivoPlantSendimeis,"imei");
        Set<String> imeis=map.keySet();
//        for(List<String> imeiList:Lists.partition(new ArrayList<String>(imeis),1500)){
//            Set<String> localImeis = vivoPlantSendimeiRepository.findImeis(imeiList);
//            for(String imei:imeiList){
//                if(!localImeis.contains(imei)){
//                    list.add(map.get(imei));
//                }
//            }
//        }
//        if(CollectionUtil.isNotEmpty(list)){
//            for(List<VivoPlantSendimei> vivoPlantSendimeisList :Lists.partition(list,1500)) {
//                vivoPlantSendimeiRepository.save(vivoPlantSendimeisList);
//            }
//        }
        return "发货串码同步成功，共同步条数据";
    }


    //查询电子保卡
    @LocalDataSource
    public String pullPlantElectronicsns(List<VivoPlantElectronicsn> vivoPlantElectronicsns) {
        List<VivoPlantElectronicsn> list = Lists.newArrayList();
//        if(CollectionUtil.isNotEmpty(vivoPlantElectronicsns)) {
//            List<String> snImeis = CollectionUtil.extractToList(vivoPlantElectronicsns, "snImei");
//            List<String> localsnImeis  = vivoPlantElectronicsnRepository.findSnImeis(snImeis);
//            for(VivoPlantElectronicsn item : vivoPlantElectronicsns){
//                if( ! localsnImeis.contains(item.getSnImei())){
//                    list.add(item);
//                }
//            }
////            if(CollectionUtil.isNotEmpty(list)) {
////                vivoPlantElectronicsnRepository.save(list);
////            }
//        }
        return "电子保卡同步成功,共同步"+list.size()+"条数据";
    }

    public  List<VivoPlantSendimei>  synIme(String date) {
//        LocalDate nowDate= LocalDateUtils.parse(date);
//        LocalDate dateStart = nowDate.minusDays(1);
//        LocalDate dateEnd = nowDate.plusDays(1);
//        List<String>  mainCodes=Lists.newArrayList();
//        mainCodes.add("M13E00");
//        List<VivoPlantSendimei> vivoPlantSendimeis = vivoPlantSendimeiRepository.findSynList(dateStart, dateEnd, mainCodes);
        return null;
    }

    @Transactional(readOnly = true)
    public FactoryOrderDto factoryOrder(FactoryOrderDto factoryOrderDto){
        List<String> factoryCodeList = Lists.newArrayList(CharConstant.JX_VIVO_FACTORY_AGENT_CODES.split(CharConstant.COMMA));
        List<String> factoryNameList = Lists.newArrayList(CharConstant.JX_VIVO_FACTORY_AGENT_NAMES.split(CharConstant.COMMA));
        if(CollectionUtil.isNotEmpty(factoryCodeList)&&CollectionUtil.isNotEmpty(factoryNameList)){
            Map<String,String> map = Maps.newHashMap();
            for(int i = 0; i < factoryCodeList.size(); i++){
                map.put(factoryCodeList.get(i),factoryNameList.get(i));
            }
            factoryOrderDto.getExtra().put("factoryCodeMap",map);
        }
        if(StringUtils.isNotBlank(factoryOrderDto.getFactoryCode())&&StringUtils.isNotBlank(factoryOrderDto.getFactoryPassword())){
            factoryOrderDto.setCode(vivoFactoryDes(factoryOrderDto.getFactoryCode()));
            factoryOrderDto.setPassword(vivoFactoryDes(factoryOrderDto.getFactoryPassword()));
        }
        return factoryOrderDto;
    }

    private String vivoFactoryDes(String code){
        Runtime runtime = Runtime.getRuntime();
        Process process;
        String command= "c:\\vivoDes.exe "+ code;
        System.out.println(command);
        try {
            process = runtime.exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String inline;
            while ((inline = br.readLine()) != null) {
                sb.append(inline.trim());
            }
            br.close();
            process.destroy();
            System.out.println(sb.toString());
            return sb.toString().trim();
        } catch (IOException e) {
            return "";
        }
    }

}
