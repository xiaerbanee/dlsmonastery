package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.Town
import org.springframework.data.jpa.repository.Query

interface TownRepository : BaseRepository<Town, String> {

    @Query("select t from #{#entityName} t where t.townName like %?1% limit 0,20")
    fun findByNameLike(name: String): MutableList<Town>


}