package ru.it.vs.info_bot_api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@FieldNameConstants
@Table(name = User.TABLE_NAME)
public class User {

    public static final String TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    @Column(name = Fields.id, updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = Fields.username, length = Integer.MAX_VALUE, nullable = false)
    private String username;

    @Column(name = Fields.chatId, nullable = false, unique = true)
    private Long chatId;

    @Column(name = Fields.phone, length = 14, nullable = false)
    private String phone;

    @Column(name = Fields.isAdmin)
    private boolean isAdmin;

}
