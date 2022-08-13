package com.example.userlogin.signin.emailtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
//    @Query("SELECT * FROM ")
    Optional<ConfirmationToken> findByToken(String token);
}
