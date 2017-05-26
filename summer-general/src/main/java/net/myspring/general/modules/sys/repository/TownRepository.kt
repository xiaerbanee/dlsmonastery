package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.Town
import org.springframework.data.jpa.repository.Query

interface TownRepository : BaseRepository<Town, String> {


    fun findTop20ByTownNameLike(townName: String): MutableList<Town>


}