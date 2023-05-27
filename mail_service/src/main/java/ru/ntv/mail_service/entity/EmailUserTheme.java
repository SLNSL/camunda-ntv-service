package ru.ntv.mail_service.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
@Table(name = "email_user_theme", uniqueConstraints = {
        @UniqueConstraint(
                name = "UniqueSubscription",
                columnNames = { "user_id", "theme_id" }
        )
})
public class EmailUserTheme implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "theme_id")
    private Integer themeId;
    
    public EmailUserTheme(Integer userId, Integer themeId){
        this.userId = userId;
        this.themeId = themeId;
    }
}