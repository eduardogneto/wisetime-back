package com.wisetime.wisetime.repository.balance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wisetime.wisetime.DTO.balance.UserBalanceDTO;
import com.wisetime.wisetime.models.balance.UserBalance;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

	@Query("SELECT ub FROM UserBalance ub WHERE ub.user.id = :userId")
	UserBalance findByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT COUNT(*) FROM user_balances ub JOIN users u ON ub.user_id = u.id WHERE u.team_id = :teamId AND ub.total_balance_in_seconds > 0", nativeQuery = true)
	long countUsersWithPositiveBalanceByTeamId(@Param("teamId") Long teamId);
	
	@Query(value = "SELECT COUNT(*) FROM user_balances ub JOIN users u ON ub.user_id = u.id WHERE u.team_id = :teamId AND ub.total_balance_in_seconds < 0", nativeQuery = true)
	long countUsersWithNegativeBalanceByTeamId(@Param("teamId") Long teamId);
	
	@Query(value = "SELECT SUM(ub.total_balance_in_seconds) FROM user_balances ub " +
            "JOIN users u ON ub.user_id = u.id " +
            "WHERE u.team_id = :teamId", nativeQuery = true)
	Long sumTotalBalanceByTeamId(@Param("teamId") Long teamId);
	
	@Query("SELECT new com.wisetime.wisetime.DTO.balance.UserBalanceDTO(u.name, ub.totalBalanceInSeconds) " +
		       "FROM UserBalance ub JOIN ub.user u " +
		       "WHERE u.team.id = :teamId")
	List<UserBalanceDTO> findUserBalancesByTeamId(@Param("teamId") Long teamId);


}
