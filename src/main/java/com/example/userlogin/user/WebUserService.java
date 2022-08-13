package com.example.userlogin.user;

import com.example.userlogin.signin.emailtoken.ConfirmationToken;
import com.example.userlogin.signin.emailtoken.ConfirmationTokenRepository;
import com.example.userlogin.signin.emailtoken.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WebUserService implements UserDetailsService{
    private final WebUserRepository webUserRepository;
    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return webUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }
    public String registerUser(WebUser user){
        boolean userExists = webUserRepository.findByEmail(user.getEmail()).isPresent();
        if(userExists){
            throw new IllegalStateException("User with E-Mail already exists " + user.getEmail());
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        webUserRepository.save(user);

        String genRandomToken = UUID.randomUUID().toString();
        confirmationTokenService.saveConfirmationToken(new ConfirmationToken(
                genRandomToken,
                LocalDateTime.now(),
                user
        ));

        return "User created and token created "+genRandomToken;
    }

    public void enableWebUser(String email){
        WebUser curUser = webUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("E-Mail not found!"));
        curUser.setEnabled(true);
//        curUser.setLocked(true);

    }
}
