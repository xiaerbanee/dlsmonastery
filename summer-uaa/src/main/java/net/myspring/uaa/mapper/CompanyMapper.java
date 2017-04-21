package net.myspring.uaa.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * Created by wangzm on 2017/4/12.
 */
@Mapper
public interface CompanyMapper{

    String findNameById(String id);

}
