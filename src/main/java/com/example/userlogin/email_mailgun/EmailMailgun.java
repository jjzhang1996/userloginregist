package com.example.userlogin.email_mailgun;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
@Getter
public class EmailMailgun {
    @Value("${mailgun.public_key}")
    private String publicKey;
    @Value("${mailgun.private_key}")
    private String apiKey;
    @Value("${mailgun.base_url}")
    private String baseUrl;
    @Value("${mailgun.from_email}")
    private String fromEmail;
    @Value("${mailgun.domain}")
    private String domain;

}
