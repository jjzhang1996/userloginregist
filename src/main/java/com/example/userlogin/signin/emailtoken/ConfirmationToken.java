package com.example.userlogin.signin.emailtoken;

import com.example.userlogin.user.WebUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @SequenceGenerator(
            name = "increment_sequence",
            sequenceName = "increment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "increment_sequence"
    )
    private long id;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private LocalDateTime localDateTime;
    @Column(nullable = false)

    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(
        nullable = false,
        name = "web_user_id"
    )
    private WebUser webUser;

    public ConfirmationToken(String token, LocalDateTime localDateTime, WebUser webUser) {
        this.token = token;
        this.localDateTime = localDateTime;
        // verification token expires after 15min
        this.expiresAt = localDateTime.plusMinutes(15);
//        this.confirmedAt = null;
        this.webUser = webUser;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
