package com.example.userlogin.email_mailgun;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EmailMailgunService {
    @Autowired
    private EmailMailgun emailMailgun;

    public String sendSimpleMessage( String toEmail, String text, String subject) {
        Configuration configuration = new Configuration()
                .domain(this.emailMailgun.getDomain())
                .apiUrl("https://api.eu.mailgun.net/v3")
                .apiKey("key-"+this.emailMailgun.getApiKey())
                .from("Support", this.emailMailgun.getFromEmail());
        Response response = Mail.using(configuration)
                .to(toEmail)
                .subject(subject)
                .text(text)
                .build()
                .send();
        System.out.println(response.responseMessage());
        return response.responseMessage();
    }

}
