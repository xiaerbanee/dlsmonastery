package net.myspring.future.modules.basic.service;

import com.google.common.collect.Maps;
import net.myspring.future.common.enums.BoolEnum;
import net.myspring.future.common.enums.DepotTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.*;
import net.myspring.future.modules.basic.web.Query.DepotQuery;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.mapper.ShopDepositMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DepotService {
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private PricesystemMapper pricesystemMapper;
    @Autowired
    private ChainMapper chainMapper;
    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private ShopDepositMapper shopDepositMapper;


    public DepotDto findOne(String id) {
        Depot depot = depotMapper.findOne(id);
        DepotDto depotDto = BeanUtil.map(depot,DepotDto.class);
        return depotDto;
    }

    public Page<DepotDto> findPage(Pageable pageable, DepotQuery depotQuery) {
        Page<DepotDto> page = depotMapper.findPage(pageable, depotQuery);
        Map<String, ShopDeposit> scbzjMap = Maps.newHashMap();
        Map<String, ShopDeposit> xxbzjMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(page.getContent())) {
            List<ShopDeposit> scbzjList = shopDepositMapper.findByTypeAndShopIds(ShopDepositTypeEnum.市场保证金.name(), CollectionUtil.extractToList(page.getContent(), "id"));
            List<ShopDeposit> xxbzjList = shopDepositMapper.findByTypeAndShopIds(ShopDepositTypeEnum.形象保证金.name(), CollectionUtil.extractToList(page.getContent(), "id"));
            xxbzjMap = CollectionUtil.extractToMap(xxbzjList, "shopId");
            scbzjMap = CollectionUtil.extractToMap(scbzjList, "shopId");
        }
        Map<Integer, String> map = DepotTypeEnum.getMap();
        for (DepotDto depotDto : page.getContent()) {
            depotDto.getDepositMap().put("xxbzj", xxbzjMap.get(depotDto.getId()) == null ? BigDecimal.ZERO : xxbzjMap.get(depotDto.getId()).getAmount());
            depotDto.getDepositMap().put("scbzj", scbzjMap.get(depotDto.getId()) == null ? BigDecimal.ZERO : scbzjMap.get(depotDto.getId()).getAmount());
            if (depotDto != null && depotDto.getType() != null) {
                depotDto.setTypeLabel(map.get(depotDto.getType()));
            }
        }
        return page;
    }

    public DepotQuery getQueryProperty(DepotQuery depotQuery) {
        depotQuery.setTypeList(DepotTypeEnum.getMap());
        depotQuery.setPricesystemList(pricesystemMapper.findAll());
        depotQuery.setChainList(chainMapper.findAll());
        depotQuery.setAdPricesystemList(adPricesystemMapper.findAll());
        depotQuery.setExpressCompanyList(expressCompanyMapper.findAll());
        depotQuery.setBools(BoolEnum.getMap());
        return depotQuery;
    }
}
