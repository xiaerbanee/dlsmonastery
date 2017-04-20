package net.myspring.general.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.modules.sys.dto.ProcessTypeDto;
import net.myspring.general.modules.sys.mapper.ProcessFlowMapper;
import net.myspring.general.modules.sys.mapper.ProcessTypeMapper;
import net.myspring.general.modules.sys.web.form.ProcessTypeForm;
import net.myspring.general.modules.sys.web.query.ProcessTypeQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProcessTypeService {

    @Autowired
    private ProcessTypeMapper processTypeMapper;

}
