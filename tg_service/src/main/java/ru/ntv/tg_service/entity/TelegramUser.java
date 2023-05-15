package ru.ntv.tg_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "telegram_user")
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "telegram_name")
    private String telegramName;

    @Column(name = "telegram_chat_id")
    private Long telegramChatId;
}