package com.spacegoat.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private Long senderUserId;
    private Long receiverUserId;
    private Double amount;
    private Timestamp timestamp;
}
