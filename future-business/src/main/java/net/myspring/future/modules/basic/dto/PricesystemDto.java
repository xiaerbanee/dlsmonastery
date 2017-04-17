package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.crm.domain.PricesystemChange;

import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class PricesystemDto extends DataDto<Pricesystem> {
    private String name;
    private Integer sort;
    private Integer version = 0;
    private List<Depot> depotList = Lists.newArrayList();
    private List<String> depotIdList = Lists.newArrayList();
    private List<PricesystemChange> pricesystemChangeList = Lists.newArrayList();
    private List<String> pricesystemChangeIdList = Lists.newArrayList();
    private List<PricesystemDetail> pricesystemDetailList = Lists.newArrayList();
    private List<String> pricesystemDetailIdList = Lists.newArrayList();
}
