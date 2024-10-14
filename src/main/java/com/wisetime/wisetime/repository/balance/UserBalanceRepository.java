package com.wisetime.wisetime.repository.balance;

import com.wisetime.wisetime.models.balance.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

	@Query("SELECT ub FROM UserBalance ub WHERE ub.user.id = :userId")
    UserBalance findByUserId(@Param("userId") Long userId);
}
