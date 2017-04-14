package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.exception.ServiceException;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.modules.sys.dto.FolderDto;
import net.myspring.basic.modules.sys.manager.FolderManager;
import net.myspring.basic.modules.sys.mapper.FolderMapper;
import net.myspring.basic.modules.sys.web.form.FolderForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class FolderService {
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private FolderMapper folderMapper;

    public Folder findByParentIdAndName(String parentId, String name) {
        return folderMapper.findByParentIdAndName(parentId, name);
    }

    public Folder findOne(String id){
        return folderManager.findOne(id);
    }

    public FolderDto findDto(String id){
        Folder folder = findOne(id);
        FolderDto folderDto = BeanUtil.map(folder, FolderDto.class);
        return folderDto;
    }

    public void deleteOne(String id){
        folderManager.deleteById(id);
    }

    @Transactional
    public Folder getRoot(String accountId) {
        Folder folder = folderMapper.findByCreatedByAndParentIds(accountId, Const.ROOT_PARENT_IDS);
        if (folder == null) {
            folder = new Folder();
            folder.setName(accountId);
            folder.setParentIds(Const.ROOT_PARENT_IDS);
            folderManager.save(folder);
        }
        return folder;
    }

    @Transactional
    public Folder getAccountFolder(String accountId, String path) {
        Folder parent = getRoot(accountId);
        Folder folder = null;
        String[] paths = path.split("/");
        for (String p : paths) {
            p = p.trim();
            if (StringUtils.isNotBlank(p)) {
                folder = folderMapper.findByParentIdAndName(parent.getId(), p);
                if (folder == null) {
                    FolderForm folderForm = new FolderForm();
                    folderForm.setParent(parent);
                    folderForm.setName(p);
                    folderForm.setParentIds(folderForm.getParent().getParentIds() + folderForm.getParent().getId() + ",");
                    folderManager.save(folder);
                }
                parent = folder;
            }
        }
        return folder;
    }

    public List<FolderDto> findAll(String accountId) {
        List<FolderDto> folderDtoList=Lists.newArrayList();
        Folder parent = getRoot(accountId);
        List<Folder> folders = Lists.newArrayList();
        List<Folder> sourcelist = folderMapper.findByCreatedBy(accountId);
        sortList(folders, sourcelist, parent.getId());
        if (!CollectionUtil.isEmpty(folders)) {
            folderDtoList= BeanUtil.map(folders,FolderDto.class);
            for (FolderDto folderDto : folderDtoList) {
                if (folderDto.getParentId() == null) {
                    folderDto.setLevelName(folderDto.getName().trim());
                } else {
                    String[] space = new String[folderDto.getParentIds().split(Const.CHAR_COMMA).length - parent.getParentIds().split(Const.CHAR_COMMA).length];
                    Arrays.fill(space, Const.CHAR_SPACE_FULL);
                    folderDto.setLevelName(StringUtils.join(space) + folderDto.getName().trim());
                }
            }
        }
        return folderDtoList;
    }

    @Transactional
    public FolderForm save(FolderForm folderForm) {
        Folder parent = folderManager.findOne(folderForm.getParentId());
        String oldParentIds = folderForm.getParentIds();
        folderForm.setParent(parent);
        folderForm.setParentIds(folderForm.getParent().getParentIds() + folderForm.getParent().getId() + ",");
        // 无法将上级部门设置为自己或者自己的下级部门
        if (!folderForm.isCreate() && folderForm.getParentIds().contains("," + folderForm.getId() + ",")) {
            throw new ServiceException("无法将上级目录设置为自己或者自己的下级目录");
        }
        folderManager.save(BeanUtil.map(folderForm,Folder.class));
        List<Folder> list = folderMapper.findByParentIdsLike("%," + folderForm.getId() + ",%");
        for (Folder e : list) {
            e.setParentIds(e.getParentIds().replace(oldParentIds, folderForm.getParentIds()));
        }
        return folderForm;
    }

    private void sortList(List<Folder> list, List<Folder> sourceList, String parentId) {
        for (int i = 0; i < sourceList.size(); i++) {
            Folder e = sourceList.get(i);
            if (e.getParentId() == null) {
                if (e.getId().equals(parentId)) {
                    list.add(e);
                }
            } else {
                if (e.getParentId().equals(parentId)) {
                    list.add(e);
                    // 判断是否还有子节点, 有则继续获取子节点
                    for (int j = 0; j < sourceList.size(); j++) {
                        Folder child = sourceList.get(j);
                        if (child.getParentId() != null && child.getParentId() != null && child.getParentId().equals(e.getId())) {
                            sortList(list, sourceList, e.getId());
                            break;
                        }
                    }
                }
            }
        }
    }
}
