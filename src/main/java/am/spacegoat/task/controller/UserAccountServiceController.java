package am.spacegoat.task.controller;

import am.spacegoat.task.dto.UserDto;
import am.spacegoat.task.service.UserAccountService;
import am.spacegoat.task.dto.FundsDto;
import am.spacegoat.task.dto.TransactionDto;
import am.spacegoat.task.dto.TransferDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserAccountServiceController {
    private final UserAccountService service;

    public UserAccountServiceController(UserAccountService service) {
        this.service = service;
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<BigDecimal> getBalanceForUserId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getBalance(id));
    }

    @PostMapping("/balance/reduce/")
    public void reduceFundsFromUser(@RequestBody @Validated(FundsDto.ReduceFunds.class) FundsDto dto) {
        service.addFunds(dto.getUserId(), dto.getAmount());
    }

    @PostMapping("/balance/add/")
    public void addFundsForUser(@RequestBody @Validated(FundsDto.AddFunds.class) FundsDto dto) {
        service.addFunds(dto.getUserId(), dto.getAmount());
    }

    @PostMapping("/transfer/")
    public void transferFunds(@RequestBody TransferDto dto) {
        service.transferRepeatableRead(dto.getSenderId(), dto.getReceiverId(), dto.getAmount());
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsForUserId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllTransactionsByUserId(id));
    }

    @GetMapping("/transactions-example/{id}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsForUserIdExampleMatcher(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllTransactionsByUserIdExampleMatcher(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Validated(UserDto.UserCreation.class) UserDto dto) {
        return ResponseEntity.ok(service.createUser(dto));
    }
}
