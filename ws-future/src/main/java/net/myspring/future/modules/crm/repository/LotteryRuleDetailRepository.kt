package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.*

import net.myspring.future.modules.crm.dto.PriceChangeImeDto
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery
import org.apache.ibatis.annotations.Param
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime


interface LotteryRuleDetailRepository : BaseRepository<LotteryRuleDetail, String> {


}
