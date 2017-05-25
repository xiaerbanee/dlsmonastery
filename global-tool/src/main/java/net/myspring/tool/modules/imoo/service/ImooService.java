package net.myspring.tool.modules.imoo.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct;
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver;
import net.myspring.tool.modules.imoo.repository.ImooRepository;
import net.myspring.tool.modules.imoo.repository.ImooPlantBasicProductRepository;
import net.myspring.tool.modules.imoo.repository.ImooPrdocutImeiDeliverRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by guolm on 2017/5/16.
 */
@Service
@Transactional
@LocalDataSource
public class ImooService {
    @Autowired
    private ImooRepository imooRepository;
    @Autowired
    private ImooPlantBasicProductRepository imooPlantBasicProductRepository;
    @Autowired
    private ImooPrdocutImeiDeliverRepository imooPrdocutImeiDeliverRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @FactoryDataSource
    @Transactional(readOnly = true)
    public List<ImooPlantBasicProduct> imooPlantBasicProducts() {
        return imooRepository.plantBasicProducts();
    }

    @FactoryDataSource
    @Transactional(readOnly = true)
    public List<ImooPrdocutImeiDeliver> plantPrdocutImeiDeliverByDate(LocalDate date) {
        String agentCodes = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(),CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
        LocalDate dateStart = date.minusDays(2);
        LocalDate dateEnd = date.plusDays(1);
        return imooRepository.plantPrdocutImeiDeliverByDate(dateStart, dateEnd, StringUtils.getSplitList(agentCodes, CharConstant.COMMA));
    }

    @LocalDataSource
    public void pullPlantProducts(List<ImooPlantBasicProduct> imooPlantBasicProducts) {
        if (CollectionUtil.isNotEmpty(imooPlantBasicProducts)) {
            List<ImooPlantBasicProduct> list = Lists.newArrayList();
            List<String> segment1s = CollectionUtil.extractToList(imooPlantBasicProducts, "segment1");
            List<String> localSegment1s = imooPlantBasicProductRepository.findSegment1s(segment1s);
            for (ImooPlantBasicProduct item : imooPlantBasicProducts) {
                if (!localSegment1s.contains(item.getSegment1())) {
                    list.add(item);
                }
            }
            if (CollectionUtil.isNotEmpty(list)) {
                imooPlantBasicProductRepository.save(list);
            }
        }
    }


    //查询发货串码
    @LocalDataSource
    public String pullPlantSendimeis(List<ImooPrdocutImeiDeliver> imooPrdocutImeiDelivers) {
        String agentCodes = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(),CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
        List<String> agentCodeList = StringUtils.getSplitList(agentCodes, CharConstant.COMMA);
        List<ImooPrdocutImeiDeliver> list = Lists.newArrayList();
        for (String agentCode : agentCodeList) {
            if (CollectionUtil.isNotEmpty(imooPrdocutImeiDelivers)) {
                List<String> imeis = CollectionUtil.extractToList(imooPrdocutImeiDelivers, "imei");
                if (CollectionUtil.isNotEmpty(imeis)) {
                    List<String> localImeis = imooPrdocutImeiDeliverRepository.findImeis(imeis);
                    for (ImooPrdocutImeiDeliver item : imooPrdocutImeiDelivers) {
                        if (!localImeis.contains(item.getImei())) {
                            item.setCompanyId(agentCode);
                            list.add(item);
                        }
                    }
                }
            }
        }
        if (CollectionUtil.isNotEmpty(list)) {
            imooPrdocutImeiDeliverRepository.save(list);
        }
        return "发货串码同步成功，共同步" + list.size() + "条数据";
    }

}
