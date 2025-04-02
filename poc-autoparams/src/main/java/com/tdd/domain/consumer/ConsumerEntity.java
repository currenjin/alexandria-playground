package com.tdd.domain.consumer;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(indexes = { @Index(columnList = "email", unique = true) })
public class ConsumerEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String email;
}
