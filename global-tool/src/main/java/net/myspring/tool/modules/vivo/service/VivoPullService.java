package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.dto.FactoryOrderDto;
import net.myspring.tool.modules.vivo.dto.VivoPlantSendimeiDto;
import net.myspring.tool.modules.vivo.repository.VivoPlantElectronicsnRepository;
import net.myspring.tool.modules.vivo.repository.VivoPlantProductsRepository;
import net.myspring.tool.modules.vivo.repository.VivoPlantSendimeiRepository;
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
import java.util.List;
import java.util.Map;

/**
 * Created by guolm on 2017/5/12.
 */
@Service
@LocalDataSource
@Transactional(readOnly = true)
public class VivoPullService {
    @Autowired
    private VivoPlantProductsRepository vivoPlantProductsRepository;
    @Autowired
    private VivoPlantSendimeiRepository vivoPlantSendimeiRepository;
    @Autowired
    private VivoPlantElectronicsnRepository vivoPlantElectronicsnRepository;

    //货品
    @FactoryDataSource
    public List<VivoPlantProducts> getPlantProducts(String companyName) {
        return vivoPlantProductsRepository.findPlantProducts(companyName);
    }

    //获取串码
    @FactoryDataSource
    public List<VivoPlantSendimei> getPlantSendimei(String date) {
        if(StringUtils.isBlank(date)){
            date= LocalDateUtils.format(LocalDate.now());
        }
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        return vivoPlantSendimeiRepository.findPlantSendimei(date, dateEnd);
    }

    //获取电子保卡
    @FactoryDataSource
    public List<VivoPlantElectronicsn> getPlantElectronicsn(String date) {
        if(StringUtils.isBlank(date)){
            date= LocalDateUtils.format(LocalDate.now());
        }
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        return vivoPlantElectronicsnRepository.findPlantElectronicsn(date,dateEnd);
    }

    //获取物料编码
    @LocalDataSource
    @Transactional
    public void pullPlantProducts( List<VivoPlantProducts> vivoPlantProducts,String companyName){
        if(CollectionUtil.isNotEmpty(vivoPlantProducts)) {
            for(VivoPlantProducts plantProduct:vivoPlantProducts){
                plantProduct.setItemNumber(plantProduct.getItemNumber().trim());
            }
            List<String> itemNumbers =CollectionUtil.extractToList(vivoPlantProducts, "itemNumber");
            List<String> localItemNumbers = CollectionUtil.extractToList(vivoPlantProductsRepository.findItemNumbers(itemNumbers,companyName),"itemNumber");
            List<VivoPlantProducts> list= Lists.newArrayList();
            for(VivoPlantProducts plantProduct : vivoPlantProducts){
                if(!localItemNumbers.contains(plantProduct.getItemNumber())){
                    plantProduct.setCompanyName(companyName);
                    list.add(plantProduct);
                }
            }
            if(CollectionUtil.isNotEmpty(list)) {
                vivoPlantProductsRepository.save(list);
            }
        }
    }

    //同步发货串码
    @LocalDataSource
    @Transactional
    public void pullPlantSendimeis(List<VivoPlantSendimei> vivoPlantSendimeis){
        if(CollectionUtil.isNotEmpty(vivoPlantSendimeis)){
            List<String> imeiList = CollectionUtil.extractToList(vivoPlantSendimeis,"imei");
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
            for(VivoPlantSendimei plantSendimei:vivoPlantSendimeis){
                if(!localImeiList.contains(plantSendimei.getImei())){
                    pullPlantSendimeis.add(plantSendimei);
                }
            }
            if(CollectionUtil.isNotEmpty(pullPlantSendimeis)){
                vivoPlantSendimeiRepository.batchSave(pullPlantSendimeis);
            }
        }
    }


    //同步电子保卡
    @LocalDataSource
    @Transactional
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
                if(!localImeiList.contains(item.getSnImei())){
                    list.add(item);
                }
            }
            if(CollectionUtil.isNotEmpty(list)) {
                vivoPlantElectronicsnRepository.save(list);
            }
        }
    }

    @LocalDataSource
    public  List<VivoPlantSendimeiDto>  getSendImeList(String date,String agentCode) {
        String dateStart =date;
        String dateEnd =LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<String>  mainCodes= StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        List<VivoPlantSendimeiDto> vivoPlantSendimeiDtos = vivoPlantSendimeiRepository.findSynList(dateStart, dateEnd, mainCodes);
        return vivoPlantSendimeiDtos;
    }


    @LocalDataSource
    public  List<VivoPlantElectronicsn>  getItemelectronSelList(String date, String agentCode) {
        String dateStart =date;
        String dateEnd =LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        List<String>  mainCodes= StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        List<VivoPlantElectronicsn> plantElectronicsns = vivoPlantElectronicsnRepository.findSynList(dateStart, dateEnd, mainCodes);
        return plantElectronicsns;
    }


    public FactoryOrderDto factoryOrder(FactoryOrderDto factoryOrderDto){
        String companyName = RequestUtils.getCompanyName();
        List<String> factoryCodeList;
        List<String> factoryNameList;
        if (CompanyNameEnum.IDVIVO.name().equals(companyName)){
            factoryCodeList = Lists.newArrayList(CharConstant.ID_VIVO_FACTORY_AGENT_CODES.split(CharConstant.COMMA));
            factoryNameList = Lists.newArrayList(CharConstant.ID_VIVO_FACTORY_AGENT_NAMES.split(CharConstant.COMMA));
        }else {
            factoryCodeList = Lists.newArrayList(CharConstant.JX_VIVO_FACTORY_AGENT_CODES.split(CharConstant.COMMA));
            factoryNameList = Lists.newArrayList(CharConstant.JX_VIVO_FACTORY_AGENT_NAMES.split(CharConstant.COMMA));
        }
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
            return sb.toString().trim();
        } catch (IOException e) {
            return "";
        }
    }

}
