package am.spacegoat.task.migration;

import am.spacegoat.task.service.UserAccountService;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.math.BigDecimal;

public class V3__transactions_init extends BaseJavaMigration {

    private UserAccountService service;

    public V3__transactions_init(UserAccountService service) {
        this.service = service;
    }

    @Override
    public void migrate(Context context) {
        service.transfer(1, 2, BigDecimal.valueOf(100));
        service.transfer(2, 5, BigDecimal.valueOf(50.25));
        service.transfer(3, 7, BigDecimal.valueOf(75.50));
        service.transfer(2, 3, BigDecimal.valueOf(30.75));
    }
}