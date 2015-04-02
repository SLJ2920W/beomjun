package com.peoplewiki.security.springSecurity.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import com.peoplewiki.security.springSecurity.domain.MemberInfo;

public class CustomJdbcDaoImpl extends JdbcDaoImpl {
	
	// 해당 사용자의 권한 정보 조회 쿼리 디폴트..미 사용
	public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY =
            "select g.id, g.group_name, ga.authority " +
            "from groups g, group_members gm, group_authorities ga " +
            "where gm.username = ? " +
            "and g.id = ga.group_id " +
            "and g.id = gm.group_id";
	
	// 해당 사용자의 권한 정보 조회 쿼리
	private String groupAuthoritiesByUsernameQuery;

	public String getGroupAuthoritiesByUsernameQuery() {
		return groupAuthoritiesByUsernameQuery;
	}

	public void setGroupAuthoritiesByUsernameQuery(String groupAuthoritiesByUsernameQuery) {
		this.groupAuthoritiesByUsernameQuery = groupAuthoritiesByUsernameQuery;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		List<UserDetails> users = loadUsersByUsername(username);

		// 해당 아이디가 존재 하지 않음
		if (users.size() == 0) {
			logger.debug("Query returned no results for user '" + username + "'");

			UsernameNotFoundException ue = new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound",
					new Object[] { username }, "Username {0} not found"));
			throw ue;
		}

		MemberInfo user = (MemberInfo) users.get(0); // contains no
														// GrantedAuthority[]

		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

		// 권한 설정 사용시 ( 무조건 true )
		if (getEnableAuthorities()) {
			dbAuthsSet.addAll(loadUserAuthorities(user.getUsername()));
		}

		// 그룹 권한 사용 시
		if (getEnableGroups()) {
			dbAuthsSet.addAll(loadGroupAuthorities(user.getUsername()));
		}

		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);
		user.setAuthorities(dbAuths);

		// 개인 권한 혹은 그룹 권한 둘다 없는 경우
		if (dbAuths.size() == 0) {
			logger.debug("User '" + username + "' has no authorities and will be treated as 'not found'");

			UsernameNotFoundException ue = new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.noAuthority",
					new Object[] { username }, "User {0} has no GrantedAuthority"));
			throw ue;
		}

		return user;
	}

	// 사용자 조회
	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(getUsersByUsernameQuery(), new String[] { username }, new RowMapper<UserDetails>() {
			public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				String username = rs.getString(1);
				String password = rs.getString(2);
				String name = rs.getString(3);
				return new MemberInfo(username, password, name, AuthorityUtils.NO_AUTHORITIES);
			}

		});
	}

	// 해당 사용자의 권한 정보 조회
	@Override
	protected List<GrantedAuthority> loadUserAuthorities(String username) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(getAuthoritiesByUsernameQuery(), new String[] { username }, new RowMapper<GrantedAuthority>() {
			public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
				String roleName = getRolePrefix() + rs.getString(1);

				return new SimpleGrantedAuthority(roleName);
			}
		});
	}

	// 해당 사용자의 그룹 권한 정보 조회
	@Override
	protected List<GrantedAuthority> loadGroupAuthorities(String username) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(groupAuthoritiesByUsernameQuery, new String[] { username }, new RowMapper<GrantedAuthority>() {
			public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
				String roleName = getRolePrefix() + rs.getString(1);

				return new SimpleGrantedAuthority(roleName);
			}
		});
	}
}
