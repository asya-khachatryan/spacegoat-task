package com.spacegoat.task.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender_user_id")
    private Long senderUserId;

    @Column(name = "receiver_user_id")
    private Long receiverUserId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "timestamp")
    private Timestamp timestamp;
}
