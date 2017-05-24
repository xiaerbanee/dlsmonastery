package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.Folder

interface FolderRepository : BaseRepository<Folder, String> {

    fun findByParentIdAndName(parentId: String, name: String): Folder

    fun findByCreatedByAndParentIds(createdBy: String, parentIds: String): Folder

    fun findByCreatedBy(createdBy: String): List<Folder>

    fun findByParentIdsLike(parentIds: String): List<Folder>
}