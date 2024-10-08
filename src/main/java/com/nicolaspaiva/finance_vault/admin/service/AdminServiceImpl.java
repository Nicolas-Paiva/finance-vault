package com.nicolaspaiva.finance_vault.admin.service;

import com.nicolaspaiva.finance_vault.admin.dto.AdminDashboard;
import com.nicolaspaiva.finance_vault.transaction.repository.TransactionRepository;
import com.nicolaspaiva.finance_vault.transaction.service.TransactionService;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import com.nicolaspaiva.finance_vault.user.repository.UserRepository;
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



    @Override
    public List<UserEntity> getAllUsers(String sort) {

        if(sort.equals("asc")){
            return userRepository.findAllByOrderByIdAsc();
        } else {
            return userRepository.findAllByOrderByIdDesc();
        }
    }

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

                .monthlyTransactions(transactionService
                        .getMonthlyTransactionNumber())

                .monthlyTransactionVolume(transactionService
                        .getMonthlyTransactionVolume())

                .monthlyRevenue(transactionService.getMonthlyTransactionFees())

                .build();
    }
}
