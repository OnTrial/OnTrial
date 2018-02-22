package org.developer.wwb.core.constants;

public class UserConstants {
	public enum ROLE {
		COMMON("0000","用户"), ADMIN("0001","超级管理员"), DEVELOPER("0002","实施"), FINANCE("0003","财务"),SALER("0004","销售");
		private String roleCode;
		private String roleName;
		ROLE(String roleCode,String roleName) {
			this.roleCode = roleCode;
			this.roleName=roleName;
		}
		@Override
		public String toString() {
			return this.roleCode;
		}
		public String getRoleCode() {
			return roleCode;
		}
		public String getRoleName() {
			return roleName;
		}
	}
}
