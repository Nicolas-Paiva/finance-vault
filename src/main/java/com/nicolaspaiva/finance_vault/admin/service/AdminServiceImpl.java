package com.nicolaspaiva.finance_vault.admin.service;

import com.nicolaspaiva.finance_vault.admin.dto.AdminDashboard;
import com.nicolaspaiva.finance_vault.transaction.repository.TransactionRepository;
import com.nicolaspaiva.finance_vault.transaction.service.TransactionService;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import com.nicolaspaiva.finance_vault.user.repository.UserRepository;
import com.nicolaspaiva.finance_vault.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final TransactionService transactionService;

    private final TransactionRepository transactionRepository;

    private final UserAccountService userService;


    @Override
    public List<UserEntity> getAllUsers(String sort) {

        if(sort.equals("asc")){
            return userRepository.findAllByOrderByIdAsc();
        } else {
            return userRepository.findAllByOrderByIdDesc();
        }
    }


    @Override
    public Optional<UserEntity> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    /**
     * Gets the admin dashboard
     */
    @Override
    public AdminDashboard getDashboard() {

        return AdminDashboard.builder()

                .numberOfUsers(userRepository.count())

                .monthlyActiveUsers(userService.countMonthlyActiveUsers())

                .newMonthlyUsers(userService.countNewMonthlyUsers())

                .monthlyTransactions(transactionService
                        .countMonthlyTransactions())

                .monthlyTransactionVolume(transactionService
                        .getMonthlyTransactionVolume())

                .monthlyRevenue(transactionService.getMonthlyTransactionFees())

                .build();
    }
}
