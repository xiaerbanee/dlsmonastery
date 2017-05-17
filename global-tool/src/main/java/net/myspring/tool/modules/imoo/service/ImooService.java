package net.myspring.tool.modules.imoo.service;

import com.google.common.collect.Lists;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.Const;
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct;
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver;
import net.myspring.tool.modules.imoo.mapper.ImooMapper;
import net.myspring.tool.modules.imoo.mapper.ImooPlantBasicProductMapper;
import net.myspring.tool.modules.imoo.mapper.ImooPrdocutImeiDeliverMapper;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by guolm on 2017/5/16.
 */
@Service
@LocalDataSource
public class ImooService {
    @Autowired
    private ImooMapper imooMapper;
    @Autowired
    private ImooPlantBasicProductMapper imooPlantBasicProductMapper;
    @Autowired
    private ImooPrdocutImeiDeliverMapper imooPrdocutImeiDeliverMapper;

    public String getCodes(String type){
        return Const.CompanyConfig.getMap().get("JXIMOO"+Const.CharEnum.UNDER_LINE.getValue()+type);
    }

    @FactoryDataSource
    public List<ImooPlantBasicProduct> imooPlantBasicProducts(){
        return imooMapper.plantBasicProducts();
    }

    @FactoryDataSource
    public List<ImooPrdocutImeiDeliver> plantPrdocutImeiDeliverByDate(List<String> agentCodes, LocalDate date){
        LocalDate dateStart = date.minusDays(2);
        LocalDate dateEnd = date.plusDays(1);
        return imooMapper.plantPrdocutImeiDeliverByDate(dateStart,dateEnd,agentCodes);
    }

    @LocalDataSource
    @Transactional(readOnly = false)
    public void pullPlantProducts(List<ImooPlantBasicProduct> imooPlantBasicProducts){
        if(CollectionUtil.isNotEmpty(imooPlantBasicProducts)) {
            List<ImooPlantBasicProduct> list = Lists.newArrayList();
            List<String> segment1s = CollectionUtil.extractToList(imooPlantBasicProducts, "segment1");
            List<String> localSegment1s = imooPlantBasicProductMapper.findSegment1s(segment1s);
            for(ImooPlantBasicProduct item : imooPlantBasicProducts){
                if( ! localSegment1s.contains(item.getSegment1())){
                    list.add(item);
                }
            }
            if(CollectionUtil.isNotEmpty(list)){
                imooPlantBasicProductMapper.save(list);
            }
        }
    }


    //查询发货串码
    @LocalDataSource
    @Transactional(readOnly = false)
    public String pullPlantSendimeis(List<ImooPrdocutImeiDeliver> imooPrdocutImeiDelivers){
        String agentCodeStr = getCodes("FACTORY_AGENT_CODES");
        List<String> agentCodes = Arrays.asList(agentCodeStr.split(Const.CharEnum.COMMA.getValue()));
        List<ImooPrdocutImeiDeliver> list = Lists.newArrayList();
        for(String agentCode:agentCodes) {
            if(CollectionUtil.isNotEmpty(imooPrdocutImeiDelivers)) {
                List<String> imeis = CollectionUtil.extractToList(imooPrdocutImeiDelivers, "imei");
                if(CollectionUtil.isNotEmpty(imeis)){
                    List <String> localImeis = imooPrdocutImeiDeliverMapper.findImeis(imeis);
                    for(ImooPrdocutImeiDeliver item : imooPrdocutImeiDelivers){
                        if( !localImeis.contains(item.getImei())){
                            item.setCompanyId(agentCode);
                            list.add(item);
                        }
                    }
                }
            }
        }
        if (CollectionUtil.isNotEmpty(list)) {
            imooPrdocutImeiDeliverMapper.save(list);
        }
        return "发货串码同步成功，共同步"+list.size()+"条数据";
    }

}
