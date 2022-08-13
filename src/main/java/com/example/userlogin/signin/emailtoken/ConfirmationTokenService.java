package com.example.userlogin.signin.emailtoken;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }
    public Optional<ConfirmationToken> findToken(String token){
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);
        return confirmationToken;
    }
    @Transactional
    public void deleteConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.deleteById(token.getId());
    }
}
