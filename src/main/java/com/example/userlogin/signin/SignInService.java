package com.example.userlogin.signin;

import com.example.userlogin.signin.emailtoken.ConfirmationToken;
import com.example.userlogin.signin.emailtoken.ConfirmationTokenService;
import com.example.userlogin.user.WebUser;
import com.example.userlogin.user.WebUserRepository;
import com.example.userlogin.user.WebUserRole;
import com.example.userlogin.user.WebUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SignInService {
    private final EmailValidator emailValidator;
    private final WebUserService webUserService;
    private final ConfirmationTokenService confirmationTokenService;
    public String register(SignInRequest request) {
        boolean valid = emailValidator.test(request.getEmail());
        if (valid) {
            return webUserService.registerUser(
                    new WebUser(
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPassword(),
                            WebUserRole.USER
                    ));
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
        // delete confirmationtoken
        confirmationTokenService.deleteConfirmationToken(confirmationToken);
        return "User successfully verified!";
    }
}
