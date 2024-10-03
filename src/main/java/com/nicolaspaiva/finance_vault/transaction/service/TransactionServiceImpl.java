package com.nicolaspaiva.finance_vault.transaction.service;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.bankaccount.repository.BankAccountRepository;
import com.nicolaspaiva.finance_vault.transaction.dto.request.TransactionRequestDto;
import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionResponseDto;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionType;
import com.nicolaspaiva.finance_vault.transaction.repository.TransactionRepository;
import com.nicolaspaiva.finance_vault.user.service.UserAccountService;
import com.nicolaspaiva.finance_vault.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles all the functionality
 * regarding transactions between
 * bank accounts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserAccountService userAccountService;


    // TODO: Implement transfer fee for some transfers

    /**
     * Process a transaction between two accounts
     *
     * The method returns different instances of
     * the TransactionResponseDto based on the
     * status of the transaction.
     */
    public TransactionResponseDto processTransaction(String email,
                                                     TransactionRequestDto transactionRequest){

        // Retrieves the sender's balance
        Optional<BankAccountEntity> senderAcc = getAccountByEmail(email);

        if(senderAcc.isEmpty()){
            throw new RuntimeException("Unexpected error");
        }

        if(!isAmountValid(transactionRequest.getAmount())){
            return TransactionResponseDto.invalidAmount();
        }

        if(!accountHasFunds(senderAcc.get(), transactionRequest.getAmount())){
            return TransactionResponseDto.notEnoughFunds();
        }

        Optional<BankAccountEntity> receiverAcc =
                getAccountByEmail(transactionRequest.getDestinationEmail());

        if(receiverAcc.isEmpty()){
            return TransactionResponseDto.accountNotFound();
        }

        if(isSelfTransaction(email, transactionRequest.getDestinationEmail())){
            return TransactionResponseDto.invalidTransaction();
        }

        TransactionEntity transaction = createTransaction(transactionRequest,
                receiverAcc.get(), senderAcc.get());

        modifyFunds(receiverAcc.get(), senderAcc.get(), transactionRequest.getAmount());

        transactionRepository.save(transaction);

        return TransactionResponseDto.transactionSuccessful();
    }


    /**
     * Returns a list with either withdrawals
     * or deposits
     */
    public List<Float> getUserMonthlyTransactionValuesByEmail(String email, TransactionType transactionType){

        int userId = userAccountService.getUserIdByEmail(email);

        LocalDateTime firstDayOfTheMonth = DateUtils.getFirstDayOfTheMonth();

        List<TransactionEntity> transactionEntities;

        // Retrieves transaction entities
        if(transactionType.equals(TransactionType.WITHDRAWAL)){
            transactionEntities = transactionRepository.findWithdrawalsByIdAndDateRange
                    (userId, firstDayOfTheMonth, LocalDateTime.now());
        } else {
            transactionEntities = transactionRepository.findDepositsByIdAndDateRange
                    (userId, firstDayOfTheMonth, LocalDateTime.now());
        }

        return getAllTransactionValues(transactionEntities);
    }


    /**
     * Returns a list with all the values
     * of the transaction in the
     * transaction entities
     */
    private List<Float> getAllTransactionValues(List<TransactionEntity> transactionEntities){
        List<Float> values = new ArrayList<>();

        for(TransactionEntity transactionEntity : transactionEntities){
            values.add(transactionEntity.getAmount());
        }

        return values;
    }


    /**
     * Creates a transaction entity based on a transaction request
     */
    private TransactionEntity createTransaction(TransactionRequestDto transactionRequest,
                                                BankAccountEntity receiverAcc,
                                                BankAccountEntity senderAcc){
        return TransactionEntity.builder()
                .receiverAccountId(receiverAcc)
                .senderAccountId(senderAcc)
                .receiverEmail(receiverAcc.getOwnerEmail())
                .senderEmail(senderAcc.getOwnerEmail())
                .amount(transactionRequest.getAmount())
                .createdAt(LocalDateTime.now())
                .build();
    }


    /**
     * Modify the funds for both sender and receiver accounts
     */
    private void modifyFunds(BankAccountEntity receiverAcc, BankAccountEntity senderAcc, float amount){
        addFunds(receiverAcc, amount);
        removeFunds(senderAcc, amount);
    }


    /**
     * Checks whether the user is trying to send money to themselves
     */
    private boolean isSelfTransaction(String senderEmail, String receiverEmail){
        return senderEmail.equals(receiverEmail);
    }


    private boolean isAmountValid(float amount){
        return amount > 0;
    }


    private boolean accountHasFunds(BankAccountEntity bankAccountEntity, float amount){
        return bankAccountEntity.getBalance() > amount;
    }


    /**
     * Retrieves an account based on the provided email
     */
    private Optional<BankAccountEntity> getAccountByEmail(String email){
        return userAccountService.getBankAccountByEmail(email);
    }


    /**
     * Adds funds to a bank account
     */
    private void addFunds(BankAccountEntity account, float amount){
        account.setBalance(account.getBalance() + amount);
    }


    /**
     * Removes funds from an account
     */
    private void removeFunds(BankAccountEntity account, float amount){
        account.setBalance(account.getBalance() - amount);
    }
}
