package net.myspring.tool.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum AgentCodeEnum {
    R250082,
    R2500821,
    R2500822,
    R2500823;

    private static List<String> list = Lists.newArrayList();

    public static List<String> getList() {
        if (CollectionUtil.isEmpty(list)) {
            for (AgentCodeEnum each : AgentCodeEnum.values()) {
                list.add(each.name());
            }
        }
        return list;
    }

}
