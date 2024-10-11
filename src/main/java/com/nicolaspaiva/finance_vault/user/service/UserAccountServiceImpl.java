package com.nicolaspaiva.finance_vault.user.service;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.bankaccount.repository.BankAccountRepository;
import com.nicolaspaiva.finance_vault.exception.UserNotFoundException;
import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionDetailsDto;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionType;
import com.nicolaspaiva.finance_vault.user.repository.UserRepository;
import com.nicolaspaiva.finance_vault.user.dto.UserAccountDto;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;

    private final BankAccountRepository bankAccountRepository;

    /**
     * Retrieves the user profile,
     * which contains the information
     * displayed in the user's home page
     */
    @Override
    public UserAccountDto getUserProfile(String email) {

        UserEntity user;
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        // Handles cases where the user does not exist
        if(userOpt.isEmpty()){
            throw new UserNotFoundException("User not found");
        } else {
            user = userOpt.get();
        }

        List<TransactionDetailsDto> deposits =
                getAccountTransactions(user, TransactionType.DEPOSIT);

        List<TransactionDetailsDto> withdrawals =
                getAccountTransactions(user, TransactionType.WITHDRAWAL);

        return UserAccountDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .balance(user.getAccount().getBalance())
                .deposits(deposits)
                .withdrawals(withdrawals)
                .build();
    }


    /**
     * Returns a list with all the deposits
     * or withdrawals of the user.
     *
     * @param transactionType indicates the type
     * of transaction, which can either be a deposit
     * or a withdrawal
     */
    private List<TransactionDetailsDto> getAccountTransactions
    (UserEntity user, TransactionType transactionType){

        if(transactionType.equals(TransactionType.WITHDRAWAL)){
            List<TransactionDetailsDto> withdrawals = user.getAccount().getWithdrawals()
                    .stream().map(TransactionEntity::entityToTransactionDetails).toList();

            withdrawals.forEach(withdrawal -> withdrawal.setAmount(withdrawal.getAmount() * -1));

            return withdrawals;
        }

        return user.getAccount().getDeposits()
                .stream().map(TransactionEntity::entityToTransactionDetails).toList();
    }


    @Override
    public void activateUser(UserEntity user){
        user.setActive(true);
    }


    @Override
    public void saveUser(UserEntity user){
        userRepository.save(user);
    }


    @Override
    public Optional<UserEntity> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    @Override
    public int getUserIdByEmail(String email){

        Optional<BankAccountEntity> account =
                bankAccountRepository.findBankAccountEntityByOwnerEmail(email);

        if(account.isEmpty()){
            return -1;
        }

        return account.get().getId();
    }


    /**
     * Returns a BankAccount entity based
     * on a provided email.
     */
    @Override
    public Optional<BankAccountEntity> getBankAccountByEmail(String email){
        return bankAccountRepository.findBankAccountEntityByOwnerEmail(email);
    }


    /**
     * Returns the number of active users
     * in the current month
     */
    @Override
    public long countMonthlyActiveUsers(){
        return userRepository.countMonthlyActiveUsers
                (LocalDateTime.now().withDayOfMonth(1), LocalDateTime.now());
    }


    /**
     * Returns the number of created users between
     * the first day of the month and the current day
     */
    @Override
    public long countNewMonthlyUsers(){
        return userRepository.countNewMonthlyUsers(
                LocalDateTime.now().withDayOfMonth(1),
                LocalDateTime.now());
    }


    @Override
    public boolean checkIfUserIsActiveByEmail(String email){
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        return userOpt.map(UserEntity::isActive).orElse(false);

    }


    /**
     * Deletes accounts that have not been
     * activated within one month of creation
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    private void deleteUnactivatedUsers(){
        List<UserEntity> users = userRepository
                .findUnactivatedUsersBeforeDate(LocalDateTime.now().minusMonths(1));

        userRepository.deleteAll(users);
    }
}
