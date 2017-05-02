package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.domain.Depot;

import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class ChainDto extends DataDto<Chain>{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
