package com.News.News.repositories;


import com.News.News.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    Optional<UserAccount> findByEmail(String email);

    @Query(value = "SELECT * FROM newsapp.user_accounts WHERE username LIKE CONCAT('%', :keyword, '%') and role = 'READER'", nativeQuery = true)
    List<UserAccount> searchAccounts(@Param("keyword") String keyword);
}