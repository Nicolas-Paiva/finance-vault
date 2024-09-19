package com.nicolaspaiva.finance_vault.transaction.dto.request;

import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionRequestDto {

    private String destinationEmail;

    private float amount;

}
