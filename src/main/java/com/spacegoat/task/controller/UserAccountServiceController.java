package com.spacegoat.task.controller;

import com.spacegoat.task.domain.Transaction;
import com.spacegoat.task.dto.FundsDto;
import com.spacegoat.task.dto.TransactionResponseDto;
import com.spacegoat.task.dto.TransferDto;
import com.spacegoat.task.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/users")
public class UserAccountServiceController {
    private final UserAccountService service;

    public UserAccountServiceController(UserAccountService service) {
        this.service = service;
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<Double> getBalanceForUserId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getBalance(id));
    }

    @PostMapping("/balance/")
    public void addFundsForUser(@RequestBody FundsDto dto) {
        service.addFunds(dto.getId(), dto.getAmount());
    }

    @PostMapping("/transfer/")
    public void transferFunds(@RequestBody TransferDto dto) {
        service.transfer(dto.getSenderId(), dto.getReceiverId(), dto.getAmount());
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<List<Transaction>> getAllTransactionsForUserId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllTransactionsForUserId(id));
    }

}
