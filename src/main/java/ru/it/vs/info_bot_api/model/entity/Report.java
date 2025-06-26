package ru.it.vs.info_bot_api.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static ru.it.vs.info_bot_api.model.entity.Report.TABLE_NAME;

@Getter
@Setter
@Entity
@FieldNameConstants
@Table(name = TABLE_NAME)
public class Report {

    public static final String TABLE_NAME = "reports";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    @Column(name = User.Fields.id, updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = Fields.organisationName, length = Integer.MAX_VALUE, nullable = false)
    private String organisationName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = Fields.isApproved, nullable = false)
    private boolean isApproved;

}
