package ru.ntv.mail_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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