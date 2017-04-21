package net.myspring.general.common.activiti;

import com.google.common.collect.Lists;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class CustomUserManager extends UserEntityManager {

	@Override
	public User createNewUser(String userId) {
		// TODO Auto-generated method stub
		return super.createNewUser(userId);
	}

	@Override
	public void insertUser(User user) {
		// TODO Auto-generated method stub
		super.insertUser(user);
	}

	@Override
	public void updateUser(User updatedUser) {
		// TODO Auto-generated method stub
		super.updateUser(updatedUser);
	}

	//userId必须
	@Override
	public User findUserById(String userId) {
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		UserEntity userEntity = new UserEntity();
		userEntity.setId(userId);
		userEntity.setFirstName(userId);
		userEntity.setRevision(1);
		return userEntity;
	}

	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		super.deleteUser(userId);
	}

	@Override
	public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
		/* TODO Auto-generated method stub */
		return super.findUserByQueryCriteria(query, page);
	}

	@Override
	public long findUserCountByQueryCriteria(UserQueryImpl query) {
		// TODO Auto-generated method stub
		return super.findUserCountByQueryCriteria(query);
	}

	@Override
	public List<Group> findGroupsByUser(String userId) {
		List<Group> list = Lists.newArrayList();
		return list;
	}

	@Override
	public UserQuery createNewUserQuery() {
		// TODO Auto-generated method stub
		return super.createNewUserQuery();
	}

	@Override
	public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
		// TODO Auto-generated method stub
		return super.findUserInfoByUserIdAndKey(userId, key);
	}

	@Override
	public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
		// TODO Auto-generated method stub
		return super.findUserInfoKeysByUserIdAndType(userId, type);
	}

	@Override
	public Boolean checkPassword(String userId, String password) {
		// TODO Auto-generated method stub
		return super.checkPassword(userId, password);
	}

	@Override
	public List<User> findPotentialStarterUsers(String proceDefId) {
		// TODO Auto-generated method stub
		return super.findPotentialStarterUsers(proceDefId);
	}

	@Override
	public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return super.findUsersByNativeQuery(parameterMap, firstResult, maxResults);
	}

	@Override
	public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
		// TODO Auto-generated method stub
		return super.findUserCountByNativeQuery(parameterMap);
	}
}
