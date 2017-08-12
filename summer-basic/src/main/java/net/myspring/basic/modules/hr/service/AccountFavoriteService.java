package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.AccountFavorite;
import net.myspring.basic.modules.hr.dto.AccountFavoriteDto;
import net.myspring.basic.modules.hr.repository.AccountFavoriteRepository;
import net.myspring.basic.modules.hr.web.form.AccountFavoriteForm;
import net.myspring.basic.modules.hr.web.query.AccountFavoriteQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class AccountFavoriteService {

    @Autowired
    private AccountFavoriteRepository accountFavoriteRepository;

    public Page<AccountFavoriteDto> list(Pageable pageable, AccountFavoriteQuery accountFavoriteQuery){
        Page<AccountFavoriteDto> page=accountFavoriteRepository.findPage(pageable,accountFavoriteQuery);
        return page;
    }

    @Transactional
    public AccountFavorite save(AccountFavoriteForm accountFavoriteForm){
        AccountFavorite accountFavorite;
        accountFavoriteForm.setParentIds(getParentIds(accountFavoriteForm.getParentId()));
        if(accountFavoriteForm.isCreate()){
            accountFavorite= BeanUtil.map(accountFavoriteForm,AccountFavorite.class);
            accountFavoriteRepository.save(accountFavorite);
        }else {
            accountFavorite=accountFavoriteRepository.findOne(accountFavoriteForm.getId());
            ReflectionUtil.copyProperties(accountFavoriteForm,accountFavorite);
            accountFavoriteRepository.save(accountFavorite);
        }
        return accountFavorite;
    }

    @Transactional
    public void delete(String id){
        accountFavoriteRepository.delete(id);
    }

    public List<TreeNode> findTreeNodeList(String accountId){
        List<TreeNode> treeNodeList= Lists.newArrayList();
        List<AccountFavorite> accountFavoriteList=accountFavoriteRepository.findByAccountIdAndParentIdIsNotNull(accountId);
        List<AccountFavorite> accountFavorites=accountFavoriteRepository.findByAccountIdAndParentIdIsNull(accountId);
        for(AccountFavorite accountFavorite:accountFavorites){
            TreeNode treeNode=new TreeNode(accountFavorite.getId(),accountFavorite.getName());
            treeNodeList.add(treeNode);
            setTreeNode(treeNode,accountFavoriteList,accountFavorite.getId());
        }
        return treeNodeList;
    }

    private void setTreeNode(TreeNode treeNode,List<AccountFavorite> accountFavoriteList,String parentId){
        for(AccountFavorite accountFavorite:accountFavoriteList){
            if(parentId.equals(accountFavorite.getParentId())){
                TreeNode tree=new TreeNode(accountFavorite.getId(),accountFavorite.getName());
                treeNode.getChildren().add(tree);
                setTreeNode(tree,accountFavoriteList,accountFavorite.getId());
            }
        }
    }

    private String getParentIds(String parentId){
        if(StringUtils.isBlank(parentId)){
            return null;
        }else {
            AccountFavorite accountFavorite=accountFavoriteRepository.findOne(parentId);
            return accountFavorite.getParentIds()+CharConstant.COMMA+accountFavorite+CharConstant.COMMA;
        }
    }
}
