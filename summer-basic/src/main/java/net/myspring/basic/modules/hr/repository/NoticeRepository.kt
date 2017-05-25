package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Employee

/**
 * Created by lihx on 2017/5/25.
 */
//TODO 应该是继承BaseRepository<EmployeeSalary, String>
interface NoticeRepository: BaseRepository<Employee, String>{}