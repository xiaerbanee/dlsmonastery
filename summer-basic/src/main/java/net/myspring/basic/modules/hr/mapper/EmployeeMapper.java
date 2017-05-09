package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.web.query.EmployeeQuery;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
@CacheDefaults(cacheName = "employees")
public interface EmployeeMapper extends BaseMapper<Employee,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Employee findOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method = MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Employee employee);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Employee employee);

    int updateAccountId(@Param("employeeId")String employeeId,@Param("accountId")String accountId);

    @UpdateProvider(type=MyProvider.class, method = MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Employee> findAllEnabled();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Employee> findAll();

    Page<EmployeeDto> findPage(Pageable pageable, @Param("p")EmployeeQuery employeeQuery);

    List<Employee> findByNameLike(String name);

    List<Employee> findByNameList(List<String> employeeNameList);

    List<Employee> findByStatusAndregularDate(@Param("status") String status,@Param("regularDate") LocalDateTime regularDate);

    List<Employee> findById(String id);

}
