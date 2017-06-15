package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.Town

interface TownRepository : BaseRepository<Town, String> {


    fun findTop20ByTownNameContaining(townName: String): MutableList<Town>

    fun findByIdIn(idList: MutableList<String>): MutableList<Town>


}