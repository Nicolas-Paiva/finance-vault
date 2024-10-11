package com.nicolaspaiva.finance_vault.user.repository;

import com.nicolaspaiva.finance_vault.user.entity.Role;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {


    Optional<UserEntity> findByEmail(String email);


    Optional<UserEntity> findByRole(Role role);


    List<UserEntity> findAllByOrderByIdAsc();


    List<UserEntity> findAllByOrderByIdDesc();


    @Query("SELECT COUNT(u) FROM UserEntity u WHERE " +
    "u.lastActive BETWEEN :firstDayOfTheMonth AND :today")
    long countMonthlyActiveUsers(
            @Param("firstDayOfTheMonth")LocalDateTime firstDayOfTheMonth,
            @Param("today") LocalDateTime today);


    @Query("SELECT u FROM UserEntity u WHERE " +
    "u.createdAt < :oneMonthAgo AND u.isActive = false ")
    List<UserEntity> findUnactivatedUsersBeforeDate(
            @Param("oneMonthAgo") LocalDateTime oneMonthAgo);


    @Query("SELECT COUNT(u) FROM UserEntity u WHERE " +
    "u.createdAt BETWEEN :firstDayOfTheMonth AND :today")
    long countNewMonthlyUsers(
            @Param("firstDayOfTheMonth") LocalDateTime firstDayOfTheMonth,
            @Param("today") LocalDateTime today);
}
