package com.finance_vault.finance_vault.repository.specification;

import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * Used to create all the specification queries.
 */
public class TransactionSpec {

    /**
     * Returns the transactions by the type,
     * which can be either a withdrawal or deposit
     */
    public static Specification<Transaction> byType(String type, User user) {
        return ((root, query, builder) -> {

            if (ObjectUtils.isEmpty(user) || type == null) {
                return null;
            }

            if (type.equals("deposit")) {
                return builder.equal(root.get("receiver"), user);
            }

            if (!type.equals("withdrawal")) {
                return null;
            }

            return builder.equal(root.get("sender"), user);
        });
    }


    private static Predicate userReceiverOrSenderPredicate(CriteriaBuilder builder, Root<Transaction> root, User user) {
        return builder.or(
                builder.equal(root.get("sender"), user),
                builder.equal(root.get("receiver"), user));
    }


    /**
     * Returns all the transactions by a max value.
     * The user must also be provided
     */
    public static  Specification<Transaction> byMaxValue(Float maxValue, User user) {

        return (root, query, builder) -> {

            if (ObjectUtils.isEmpty(user) || maxValue == null) {
                return null;
            }

            Predicate userPredicate = userReceiverOrSenderPredicate(builder, root, user);

            Predicate maxValuePredicate = builder.lessThanOrEqualTo(root.get("amount"), maxValue);

            return builder.and(userPredicate, maxValuePredicate);
        };

    }



    public static Specification<Transaction> byMinValue(Float minValue, User user) {

        return (root, query, builder) -> {

            if (ObjectUtils.isEmpty(user) || minValue == null) {
                return null;
            }

            Predicate userPredicate = userReceiverOrSenderPredicate(builder, root, user);

            Predicate minValuePredicate = builder.greaterThanOrEqualTo(root.get("amount"), minValue);

            return builder.and(userPredicate, minValuePredicate);
        };

    }


}
