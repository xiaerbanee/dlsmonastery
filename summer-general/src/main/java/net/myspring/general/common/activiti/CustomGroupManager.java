package net.myspring.general.common.activiti;

import com.google.common.collect.Lists;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;

import java.util.List;
import java.util.Map;

/**
 * 自定义的Activiti用户组管理器
 *
 */
public class CustomGroupManager extends GroupEntityManager {
	@Override
	public Group createNewGroup(String groupId) {
		// TODO Auto-generated method stub
		return super.createNewGroup(groupId);
	}

	@Override
	public void insertGroup(Group group) {
		// TODO Auto-generated method stub
		super.insertGroup(group);
	}

	@Override
	public void updateGroup(Group updatedGroup) {
		// TODO Auto-generated method stub
		super.updateGroup(updatedGroup);
	}

	@Override
	public void deleteGroup(String groupId) {
		// TODO Auto-generated method stub
		super.deleteGroup(groupId);
	}

	@Override
	public GroupQuery createNewGroupQuery() {
		// TODO Auto-generated method stub
		return super.createNewGroupQuery();
	}

	@Override
	public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
		// TODO Auto-generated method stub
		return super.findGroupByQueryCriteria(query, page);
	}

	@Override
	public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
		// TODO Auto-generated method stub
		return super.findGroupCountByQueryCriteria(query);
	}

	@Override
	public List<Group> findGroupsByUser(String userId) {
		List<Group> list = Lists.newArrayList();
		return list;
	}

	@Override
	public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return super.findGroupsByNativeQuery(parameterMap, firstResult, maxResults);
	}

	@Override
	public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
		// TODO Auto-generated method stub
		return super.findGroupCountByNativeQuery(parameterMap);
	}
}
