package ru.ntv.entity.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "email_user")
public class EmailUser {
    @Id
    @Column(name = "id")
    private int userId;

    @Column(name = "email")
    private String email;
}