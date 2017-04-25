package net.myspring.cloud.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.netflix.governator.annotations.binding.Mode;
import com.sun.org.apache.bcel.internal.generic.NEW;
import net.myspring.cloud.common.enums.VoucherStatusEnum;
import net.myspring.cloud.common.utils.Const;
import net.myspring.cloud.modules.sys.dto.KingdeeBookDto;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.cloud.modules.sys.web.form.KingdeeBookForm;
import net.myspring.cloud.modules.sys.web.query.KingdeeBookQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@RestController
@RequestMapping(value = "sys/kingdeeBook")
public class KingdeeBookController {
    @Autowired
    private KingdeeBookService kingdeeBookService;

    @ModelAttribute
    private KingdeeBookForm get(String id){
        KingdeeBookDto kingdeeBookDto = StringUtils.isBlank(id) ? new KingdeeBookDto() : kingdeeBookService.findOne(id);
        KingdeeBookForm kingdeeBookForm = BeanUtil.map(kingdeeBookDto,KingdeeBookForm.class);
        return kingdeeBookForm;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<KingdeeBookDto> list(Pageable pageable, KingdeeBookQuery kingdeeBookQuery){
        Page<KingdeeBookDto> page = kingdeeBookService.findPage(pageable,kingdeeBookQuery);
        for(KingdeeBookDto kingdeeBookDto : page.getContent()){
            kingdeeBookDto.setActionList(getActionList(kingdeeBookDto));
        }
        return page;
    }

    @RequestMapping(value = "findForm")
    public KingdeeBookDto findOne(String id){
        KingdeeBookDto kingdeeBookDto = kingdeeBookService.findOne(id);
        return kingdeeBookDto;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        kingdeeBookService.deleteById(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(KingdeeBookForm kingdeeBookForm){
        kingdeeBookService.save(kingdeeBookForm);
        return new RestResponse("保存成功",null);
    }

    public List<String> getActionList(KingdeeBookDto kingdeeBookDto){
        List<String> actionList = Lists.newArrayList();
        actionList.add(Const.ITEM_ACTION_EDIT);
        actionList.add(Const.ITEM_ACTION_DELETE);
        actionList.add(Const.ITEM_ACTION_DETAIL);
        return actionList;
    }
}
