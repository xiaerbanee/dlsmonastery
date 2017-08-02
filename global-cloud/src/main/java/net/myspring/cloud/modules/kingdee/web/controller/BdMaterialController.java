package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.service.BdMaterialService;
import net.myspring.cloud.modules.kingdee.web.form.BatchMaterialForm;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/5/12.
 */
@RestController
@RequestMapping(value = "kingdee/bdMaterial")
public class BdMaterialController {
    @Autowired
    private BdMaterialService bdMaterialService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;
    @Autowired
    private KingdeeBookService kingdeeBookService;

    @RequestMapping(value = "findByName")
    public BdMaterial findByName(String name){
        return bdMaterialService.findByName(name);
    }

    @RequestMapping(value = "findByNumber")
    public BdMaterial findByNumber(String number){
        return bdMaterialService.findByNumber(number);
    }

    @RequestMapping(value = "findAll")
    public List<BdMaterial> findAll( ){
        return bdMaterialService.findAll();
    }

    @RequestMapping(value = "findByMaxModifyDate")
    public List<BdMaterial> findByMaxModifyDate(String maxModifyDate){
        return bdMaterialService.findByMaxModifyDate(LocalDateTime.parse(maxModifyDate));
    }

    @RequestMapping(value = "form")
    public BatchMaterialForm form(){
        return bdMaterialService.form();
    }

    @RequestMapping(value = "batchSave")
    public RestResponse batchSave(BatchMaterialForm batchMaterialForm) {
        RestResponse restResponse;
        StringBuilder message = new StringBuilder();
        try {
            AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountIdAndCompanyName(RequestUtils.getAccountId(),RequestUtils.getCompanyName());
            KingdeeBook kingdeeBook = kingdeeBookService.findOne(accountKingdeeBook.getKingdeeBookId());
            if (accountKingdeeBook != null) {
                List<KingdeeSynDto> kingdeeSynExtendDtoList = bdMaterialService.save(batchMaterialForm, kingdeeBook, accountKingdeeBook);
                for (KingdeeSynDto kingdeeSynDto : kingdeeSynExtendDtoList) {
                    if (kingdeeSynDto.getSuccess()) {
                        message.append(kingdeeSynDto.getBillNo()+",");
                    }
                }
                restResponse = new RestResponse("物料批量保存成功：" + message, null, true);
            }else {
                restResponse = new RestResponse("您没有金蝶账号，不能开单", null, false);
            }
            return restResponse;
        }catch (Exception e){
            return new RestResponse(e.getMessage(), ResponseCodeEnum.invalid.name(), false);
        }
    }
}
