package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.Town
import org.springframework.data.jpa.repository.Query

interface TownRepository : BaseRepository<Town, String> {

    @Query("""
            SELECT t1.*
            FROM sys_town t1
            WHERE t1.town_name like %?1%
            limit 0,20
        """, nativeQuery = true)
    fun findByNameLike(name: String): List<Town>


}