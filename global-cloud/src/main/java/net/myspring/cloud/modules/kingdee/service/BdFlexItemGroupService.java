package net.myspring.cloud.modules.kingdee.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.repository.BdFlexItemGroupRepository;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 核算维度
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

    public List<String> getVoucherHeader(){
        List<String> list = Lists.newLinkedList();
        list.add("摘要");
        list.add("科目名称");

        list.add("借方金额");
        list.add("贷方金额");
        return list;
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
