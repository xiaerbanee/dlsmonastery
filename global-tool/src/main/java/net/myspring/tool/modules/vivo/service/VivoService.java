package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.domain.VivoProducts;
import net.myspring.tool.modules.vivo.dto.FactoryOrderDto;
import net.myspring.tool.modules.vivo.dto.VivoPlantSendimeiDto;
import net.myspring.tool.modules.vivo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
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
        if(StringUtils.isBlank(date)){
            date= LocalDateUtils.format(LocalDate.now());
        }
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        return vivoPlantSendimeiRepository.findPlantSendimei(date, dateEnd, agentCodes);
    }

    @FactoryDataSource
    public List<VivoPlantElectronicsn> getPlantElectronicsn(String date) {
        if(StringUtils.isBlank(date)){
            date= LocalDateUtils.format(LocalDate.now());
        }
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        return vivoPlantElectronicsnRepository.findPlantElectronicsn(date,dateEnd);
    }

    //获取颜色编码
    @LocalDataSource
    @Transactional(readOnly = false)
    public void pullVivoProducts(List<VivoProducts> vivoProducts){
        if(CollectionUtil.isNotEmpty(vivoProducts)) {
            for(VivoProducts vivoProduct:vivoProducts){
                vivoProduct.setColorId(vivoProduct.getColorId().trim());
            }
            List<String> colorIds = CollectionUtil.extractToList(vivoProducts, "colorId");
            List<String> localColorIds = CollectionUtil.extractToList( vivoProductsRepository.findColorIds(colorIds),"colorId");
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
    @LocalDataSource
    @Transactional(readOnly = false)
    public void pullPlantProducts( List<VivoPlantProducts> vivoPlantProducts){
        if(CollectionUtil.isNotEmpty(vivoPlantProducts)) {
            for(VivoPlantProducts plantProduct:vivoPlantProducts){
                plantProduct.setItemNumber(plantProduct.getItemNumber().trim());
            }
            List<String> itemNumbers =CollectionUtil.extractToList(vivoPlantProducts, "itemNumber");
            List<String> localItemNumbers = CollectionUtil.extractToList(vivoPlantProductsRepository.findItemNumbers(itemNumbers),"itemNumber");
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
    @Transactional(readOnly = false)
    public void pullPlantSendimeis(List<VivoPlantSendimei> vivoPlantSendimeis){
        if(CollectionUtil.isNotEmpty(vivoPlantSendimeis)){
            List<String> imeiList = Lists.newArrayList();
            Map<String,List<VivoPlantSendimei>> vivoPlantSendimeiMap= Maps.newHashMap();
            for(VivoPlantSendimei vivoPlantSendimei:vivoPlantSendimeis){
                if(!vivoPlantSendimeiMap.containsKey(vivoPlantSendimei.getCompanyId())){
                    List<VivoPlantSendimei> plantSendimeis=Lists.newArrayList();
                    vivoPlantSendimeiMap.put(vivoPlantSendimei.getCompanyId(),plantSendimeis);
                }
                imeiList.add(vivoPlantSendimei.getImei());
                vivoPlantSendimeiMap.get(vivoPlantSendimei.getCompanyId()).add(vivoPlantSendimei);
            }
            List<String> localImeiList=Lists.newArrayList();
            if(CollectionUtil.isNotEmpty(imeiList)){
                for(List<String>imes:CollectionUtil.splitList(imeiList,1000)){
                    List<VivoPlantSendimei> plantSendimeis=vivoPlantSendimeiRepository.findImeis(imes);
                    if(CollectionUtil.isNotEmpty(plantSendimeis)){
                        localImeiList.addAll(CollectionUtil.extractToList(plantSendimeis,"imei"));
                    }
                }
            }
            List<VivoPlantSendimei> pullPlantSendimeis=Lists.newArrayList();
            for(String agentCode:vivoPlantSendimeiMap.keySet()){
                List<VivoPlantSendimei>  plantSendimeis=vivoPlantSendimeiMap.get(agentCode);
                for(VivoPlantSendimei plantSendimei:plantSendimeis){
                    plantSendimei.setCompanyId(agentCode);
                    if(!localImeiList.contains(plantSendimei.getImei())){
                        pullPlantSendimeis.add(plantSendimei);
                    }
                }
            }
            if(CollectionUtil.isNotEmpty(pullPlantSendimeis)){
                vivoPlantSendimeiRepository.batchSave(pullPlantSendimeis);
            }
        }
    }


    //查询电子保卡
    @LocalDataSource
    public void pullPlantElectronicsns(List<VivoPlantElectronicsn> vivoPlantElectronicsns) {
        if(CollectionUtil.isNotEmpty(vivoPlantElectronicsns)) {
            List<String> snImeis = CollectionUtil.extractToList(vivoPlantElectronicsns, "snImei");
            List<String> localImeiList=Lists.newArrayList();
            if(CollectionUtil.isNotEmpty(snImeis)){
                for(List<String> imeis:CollectionUtil.splitList(snImeis,1000)){
                    List<VivoPlantElectronicsn> plantElectronicsns  = vivoPlantElectronicsnRepository.findSnImeis(imeis);
                    if(CollectionUtil.isNotEmpty(plantElectronicsns)){
                        localImeiList.addAll(CollectionUtil.extractToList(plantElectronicsns,"snImei"));
                    }
                }
            }
            List<VivoPlantElectronicsn> list = Lists.newArrayList();
            for(VivoPlantElectronicsn item : vivoPlantElectronicsns){
                if( ! localImeiList.contains(item.getSnImei())){
                    list.add(item);
                }
            }
            if(CollectionUtil.isNotEmpty(list)) {
                vivoPlantElectronicsnRepository.save(list);
            }
        }
    }

    @LocalDataSource
    public  List<VivoPlantSendimeiDto>  synIme(String date,String agentCode) {
        String dateStart =date;
        String dateEnd =LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<String>  mainCodes= StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        List<VivoPlantSendimeiDto> vivoPlantSendimeiDtos = vivoPlantSendimeiRepository.findSynList(dateStart, dateEnd, mainCodes);
        return vivoPlantSendimeiDtos;
    }


    @LocalDataSource
    public  List<VivoPlantElectronicsn>  synVivoPlantElectronicsnl(String date, String agentCode) {
        String dateStart =date;
        String dateEnd =LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<String>  mainCodes= StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        List<VivoPlantElectronicsn> plantElectronicsns = vivoPlantElectronicsnRepository.findSynList(dateStart, dateEnd, mainCodes);
        return plantElectronicsns;
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
