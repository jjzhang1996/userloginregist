package com.example.userlogin.signin;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/signin")
@AllArgsConstructor
public class SignInController {
    private SignInService signInService;
    @PostMapping
    public String signIn(@RequestBody SignInRequest request){
        return signInService.register(request);
    }
    @GetMapping(path = "/confirm/{id}")
    public String validateToken(@PathVariable String token){
        return signInService.confirmToken(token);
    }
}
