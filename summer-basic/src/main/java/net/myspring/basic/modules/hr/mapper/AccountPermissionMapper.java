package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.modules.hr.domain.AccountPermission;
import net.myspring.basic.common.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wangzm on 2017/5/3.
 */
@Mapper
public interface AccountPermissionMapper extends MyMapper<AccountPermission,String> {

    int removeByPermissionList(List<String> permissionIdList);

    List<String> findPermissionIdByAccount(String account);
}
