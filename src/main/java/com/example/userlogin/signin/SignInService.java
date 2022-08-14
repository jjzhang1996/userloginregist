package com.example.userlogin.signin;

import com.example.userlogin.email_mailgun.EmailMailgun;
import com.example.userlogin.email_mailgun.EmailMailgunService;
import com.example.userlogin.signin.emailtoken.ConfirmationToken;
import com.example.userlogin.signin.emailtoken.ConfirmationTokenService;
import com.example.userlogin.user.WebUser;
import com.example.userlogin.user.WebUserRole;
import com.example.userlogin.user.WebUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SignInService {
    private final EmailValidator emailValidator;
    private final WebUserService webUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailMailgunService emailMailgunService;


    public String register(SignInRequest request) {
        boolean valid = emailValidator.test(request.getEmail());
        if (valid) {
            String token = webUserService.registerUser(
                    new WebUser(
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPassword(),
                            WebUserRole.USER
                    ));
            String link = "http://localhost:8080/api/v1/signin/confirm/"+token;
//
            emailMailgunService.sendSimpleMessage();
            return token;
        }
        else{
            throw new IllegalStateException("E-Mail is not valid " + request.getEmail());
        }
    }
    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService
                .findToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found!"));
        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("Token already used!");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token has expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());

        webUserService.enableWebUser(confirmationToken.getWebUser().getEmail());

        // confirmationTokenService.deleteConfirmationToken(confirmationToken); DELETE after few days
        return "User successfully verified!";
    }
}
