package com.example.userlogin.signin;

import com.example.userlogin.user.WebUser;
import com.example.userlogin.user.WebUserRepository;
import com.example.userlogin.user.WebUserRole;
import com.example.userlogin.user.WebUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignInService {
    private final EmailValidator emailValidator;
    private final WebUserService webUserService;
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
}
