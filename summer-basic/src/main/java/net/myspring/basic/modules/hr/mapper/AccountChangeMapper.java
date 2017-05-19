package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.AccountChange;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.web.form.AccountChangeForm;
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface AccountChangeMapper extends MyMapper<AccountChange,String> {

    Page<AccountChangeDto> findPage(Pageable pageable, @Param("p")AccountChangeQuery accountChangeQuery);

    AccountChangeForm getForm(@Param("p")AccountChangeQuery accountChangeQuery);

}
