package org.example.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_type", length = 50, nullable = false)
    private String tokenType;

    @Column(name = "token_count", nullable = false)
    private int tokenCount = 2;

    @Column(name = "last_reset", nullable = false)
    private LocalDate lastReset;
}
