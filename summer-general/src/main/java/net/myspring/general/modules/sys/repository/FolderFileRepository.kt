package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.FolderFile
import net.myspring.general.modules.sys.dto.FolderFileDto
import net.myspring.general.modules.sys.web.query.FolderFileQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FolderFileRepository : BaseRepository<FolderFile, String>,FolderFileRepositoryCustom {

}

interface FolderFileRepositoryCustom {
    fun findAll(pageable: Pageable, folderFileQuery: FolderFileQuery): Page<FolderFile>;
}

class FolderFileRepositoryImpl:FolderFileRepositoryCustom {
    override fun findAll(pageable: Pageable, folderFileQuery: FolderFileQuery): Page<FolderFile> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}