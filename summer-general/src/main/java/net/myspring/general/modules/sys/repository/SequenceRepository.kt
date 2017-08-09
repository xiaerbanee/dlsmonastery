package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.Sequence


interface SequenceRepository : BaseRepository<Sequence, String> {

    fun findBySeqName(seqName :String): Sequence?

}
