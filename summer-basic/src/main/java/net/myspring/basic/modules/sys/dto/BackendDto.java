package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.Backend;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendDto extends DataDto<Backend> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
