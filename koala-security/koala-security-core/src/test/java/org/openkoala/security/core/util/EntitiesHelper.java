package org.openkoala.security.core.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.core.domain.OrganizationScope;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.Scope;
import org.openkoala.security.core.domain.User;

public final class EntitiesHelper {

	public static void assertUser(User expected, User actual) {
		assertEquals(expected.getCreateOwner(), actual.getCreateOwner());
		assertEquals(expected.getDescription(), actual.getDescription());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getTelePhone(), actual.getTelePhone());
		assertEquals(expected.getUserAccount(), actual.getUserAccount());
		assertEquals(expected.getLastLoginTime(), actual.getLastLoginTime());
		assertEquals(expected.getLastModifyTime(), actual.getLastModifyTime());
	}

	public static void assertRole(Role expected, Role actual) {
		assertEquals(expected.getName(),actual.getName());
		assertEquals(expected.getDescription(),actual.getDescription());
	}
	
	public static void assertPermission(Permission expected, Permission actual){
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getIdentifier(), actual.getIdentifier());
		assertEquals(expected.getDescription(), actual.getDescription());
	}

	public static User initUser() {
		User user = new User("test000000000000000001", "aaa", "test01@foreveross.com", "18665588990");
		user.setName("测试01");
		user.setLastLoginTime(new Date());
		user.setCreateOwner("admin");
		user.setDescription("测试");
		return user;
	}

	public static Role initRole() {
		Role role = new Role("testRole0000000000");
		role.setDescription("用于测试角色");
		return role;
	}

	public static Permission initPermission() {
		Permission permission = new Permission("测试权限000001", "testPermission000001");
		permission.setDescription("用于测试权限");
		return permission;
	}
	
	public static Authorization initAuthorization(Actor actor,Authority authority){
		Authorization authorization = new Authorization(actor, authority);
		return authorization;
	}
	public static Authorization initAuthorization(Actor actor,Authority authority,Scope scope){
		Authorization authorization = new Authorization(actor, authority,scope);
		return authorization;
	}
}