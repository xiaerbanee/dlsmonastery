package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.AccountFavoriteDto;
import net.myspring.basic.modules.hr.service.AccountFavoriteService;
import net.myspring.basic.modules.hr.web.form.AccountFavoriteForm;
import net.myspring.basic.modules.hr.web.query.AccountFavoriteQuery;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.common.response.RestResponse;
import net.myspring.common.tree.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "hr/accountFavorite")
public class AccountFavoriteController  {

    @Autowired
    private AccountFavoriteService accountFavoriteService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AccountFavoriteDto> list(Pageable pageable, AccountFavoriteQuery accountFavoriteQuery){
        return accountFavoriteService.list(pageable,accountFavoriteQuery);
    }

    @RequestMapping(value = "getQuery")
    public AccountFavoriteQuery getQuery(AccountFavoriteQuery accountFavoriteQuery){
        return accountFavoriteQuery;
    }

    @RequestMapping(value = "getForm")
    public AccountFavoriteForm getForm(AccountFavoriteForm accountFavoriteForm){
        accountFavoriteForm.getExtra().put("parentIdList",accountFavoriteService.findAll());
        return accountFavoriteForm;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id){
        accountFavoriteService.delete(id);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "findOne")
    public AccountFavoriteDto findOne(String id){
        return accountFavoriteService.findOne(id);
    }

    @RequestMapping(value = "save")
    public RestResponse save(AccountFavoriteForm accountFavoriteForm){
        accountFavoriteService.save(accountFavoriteForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "findTreeNodeList")
    public List<TreeNode> findTreeNodeList(){
        String accountId= RequestUtils.getAccountId();
        return accountFavoriteService.findTreeNodeList(accountId);
    }


}
