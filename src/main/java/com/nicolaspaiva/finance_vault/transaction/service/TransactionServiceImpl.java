package com.nicolaspaiva.finance_vault.transaction.service;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
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
    @Override
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

        Optional<BankAccountEntity> receiverAccOpt =
                getAccountByEmail(transactionRequest.getDestinationEmail());

        BankAccountEntity receiverAcc;

        if(receiverAccOpt.isEmpty()){
            return TransactionResponseDto.accountNotFound();
        } else {
            receiverAcc = receiverAccOpt.get();
        }

        if(isSelfTransaction(email, transactionRequest.getDestinationEmail())){
            return TransactionResponseDto.invalidTransaction();
        }

        if(!userAccountService.checkIfUserIsActiveByEmail(transactionRequest.getDestinationEmail())){
            return TransactionResponseDto.invalidTransaction("User is not active");
        }

        TransactionEntity transaction = createTransaction(transactionRequest,
                receiverAcc, senderAcc.get());

        modifyFunds(receiverAcc, senderAcc.get(), transactionRequest.getAmount());

        transactionRepository.save(transaction);

        return TransactionResponseDto.transactionSuccessful();
    }

    private double calculateFee(double transactionAmount){
        return transactionAmount * 0.05;
    }


    /**
     * Returns a list with either withdrawals
     * or deposits
     */
    @Override
    public List<Double> getUserMonthlyTransactionValuesByEmail(String email, TransactionType transactionType){

        int userId = userAccountService.getUserIdByEmail(email);

        // Returns an empty list if the user does not exist
        if(userId < 1){
            return new ArrayList<>();
        }

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
    private List<Double> getAllTransactionValues(List<TransactionEntity> transactionEntities){
        List<Double> values = new ArrayList<>();

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
                .fee(calculateFee(transactionRequest.getAmount()))
                .createdAt(LocalDateTime.now())
                .build();
    }


    /**
     * Modify the funds for both sender and receiver accounts,
     * adding a fee to the sender's account
     */
    private void modifyFunds(BankAccountEntity receiverAcc, BankAccountEntity senderAcc, double amount){
        addFunds(receiverAcc, amount);
        removeFunds(senderAcc, amount + calculateFee(amount));
    }


    /**
     * Checks whether the user is trying to send money to themselves
     */
    private boolean isSelfTransaction(String senderEmail, String receiverEmail){
        return senderEmail.equals(receiverEmail);
    }


    private boolean isAmountValid(double amount){
        return amount > 0;
    }


    private boolean accountHasFunds(BankAccountEntity bankAccountEntity, double amount){
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
    private void addFunds(BankAccountEntity account, double amount){
        account.setBalance(account.getBalance() + amount);
    }


    /**
     * Removes funds from an account
     */
    private void removeFunds(BankAccountEntity account, double amount){
        account.setBalance(account.getBalance() - amount);
    }


    /**
     * Retrieves a list with all the transactions
     * performed in the current month
     */
    @Override
    public List<TransactionEntity> getMonthlyTransactions(){
        return transactionRepository
                .getMonthlyTransactions(DateUtils.getFirstDayOfTheMonth(), DateUtils.getToday());
    }


    /**
     * Returns the number of transactions
     * performed in the current month
     */
    @Override
    public int countMonthlyTransactions(){
        return getMonthlyTransactions().size();
    }


    /**
     * Gets the volume (in €)
     * of the transactions performed
     * during the month
     */
    @Override
    public double getMonthlyTransactionVolume(){
        List<TransactionEntity> monthlyTransactions = getMonthlyTransactions();

        return monthlyTransactions.stream().mapToDouble(TransactionEntity::getAmount).sum();
    }


    /**
     * Gets all the fees obtained
     * through the transactions
     * performed in the month
     */
    @Override
    public double getMonthlyTransactionFees(){
        return getMonthlyTransactions().stream()
                        .mapToDouble(TransactionEntity::getFee).sum();
        // .reduce(0.0, Double::sum)
    }
}
