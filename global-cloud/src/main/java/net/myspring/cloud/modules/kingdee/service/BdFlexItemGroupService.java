package net.myspring.cloud.modules.kingdee.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;
import net.myspring.cloud.modules.kingdee.repository.BdFlexItemGroupRepository;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.dto.VoucherModel;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 核算维度组
 * Created by lihx on 2017/6/16.
 */
@Service
@KingdeeDataSource
public class BdFlexItemGroupService {
    @Autowired
    private BdFlexItemGroupRepository bdFlexItemGroupRepository;

    public List<BdFlexItemGroup> findAll(){
        return bdFlexItemGroupRepository.findAll();
    }


    //查询所有使用中的核算维度
    public List<String> getNameList() {
        List<BdFlexItemGroup> flexItemGroupList = bdFlexItemGroupRepository.findAll();
        List<String> list = Lists.newLinkedList();
        for (BdFlexItemGroup flexItemGroup : flexItemGroupList) {
            List<String> nameList = StringUtils.getSplitList(flexItemGroup.getFName(), CharConstant.SLASH_LINE);
            for (String name : nameList) {
                if (!list.contains(name)) {
                    list.add(name);
                }
            }
        }
        return list;
    }
}
