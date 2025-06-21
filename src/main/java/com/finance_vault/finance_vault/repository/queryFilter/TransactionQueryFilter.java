package com.finance_vault.finance_vault.repository.queryFilter;

import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import static com.finance_vault.finance_vault.repository.specification.TransactionSpec.*;

/**
 * Represents all the possible filters
 * a user can make.
 */
@Data
public class TransactionQueryFilter {

    private User user;

    private String type;

    private Float minValue;

    private Float maxValue;

    public Specification<Transaction> toSpecification() {
        if (minValue != null && maxValue != null && minValue > maxValue) {
            Float aux = maxValue;
            maxValue = minValue;
            minValue = aux;
        }

        return byType(type, user)
                .and(byMaxValue(maxValue, user))
                .and(byMinValue(minValue, user));
    }

}
