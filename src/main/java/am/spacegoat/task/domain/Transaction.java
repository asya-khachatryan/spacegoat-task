package am.spacegoat.task.domain;

import am.spacegoat.task.dto.TransactionDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    private UserEntity senderUserEntity;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private UserEntity receiverUserEntity;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "creation_time", nullable = false)
    @CreationTimestamp
    private LocalDateTime creationTime;

    public static TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setSenderUserId(transaction.getSenderUserEntity().getId());
        dto.setReceiverUserId(transaction.getReceiverUserEntity().getId());
        dto.setCreationTime(transaction.getCreationTime());
        return dto;
    }
}
