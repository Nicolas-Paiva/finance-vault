package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.PaginatedResponse;
import com.finance_vault.finance_vault.dto.summary.MonthlyTransactionsDTO;
import com.finance_vault.finance_vault.dto.summary.WeeklyTotalsDto;
import com.finance_vault.finance_vault.dto.transaction.*;
import com.finance_vault.finance_vault.exception.InvalidTransactionException;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.TransactionRepository;
import com.finance_vault.finance_vault.repository.UserRepository;
import com.finance_vault.finance_vault.repository.queryFilter.TransactionQueryFilter;
import com.finance_vault.finance_vault.service.notification.NotificationService;
import com.finance_vault.finance_vault.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    private final NotificationService notificationService;
    private final UserRepository userRepository;


    /**
     * Creates a transaction between two users.
     * A transaction is only allowed if the sender has
     * enough funds and provide a correct email address
     * for the receiver.
     * <p>
     * If the transaction is successful, a TransactionResponse with
     * success equal to true is sent to the client.
     */
    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest transactionRequest, User user) {
        float amount = transactionRequest.getAmount();

        // Check whether sender exists
        User sender = userService.getUserFromEmail(user.getEmail())
                .orElseThrow(InvalidTransactionException::senderDoesNotExist);

        // Check whether receiver exists
        User receiver = userService.getUserFromEmail(transactionRequest.getReceiverEmail())
                .orElseThrow(InvalidTransactionException::receiverDoesNotExist);

        if (sender.getEmail().equals(receiver.getEmail())) {
            throw InvalidTransactionException.selfTransactionNotAllowed();
        }

        // Check whether value is positive
        if (amount <= 0) {
            throw InvalidTransactionException.invalidAmount();
        }

        // Check whether there is enough funds
        if (user.getBalance() < amount) {
            throw InvalidTransactionException.notEnoughFunds();
        }


        // Performs the transaction
        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .build();

        transactionRepository.save(transaction);


        // Updates the sender's data
        sender.setBalance(sender.getBalance() - amount);
        sender.getSentTransactions().add(transaction);

        // Updates the receiver's data
        receiver.setBalance(receiver.getBalance() + amount);
        receiver.getReceivedTransactions().add(transaction);

        // Updates the notification service
        notificationService.addTransactionNotification(transaction);

        return TransactionResponse.success();
    }


    public PaginatedResponse<TransactionView>
    getFilteredTransactions(int page,
                            int size,
                            TransactionQueryFilter filter,
                            String sortBy,
                            String order) {

        Pageable pageable;

        List<String> allowedFields = List.of("createdAt", "amount");

        if (!allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        if (order.equalsIgnoreCase("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        Specification<Transaction> spec = filter.toSpecification();

        Page<TransactionView> transactions = transactionRepository.findAll(spec, pageable)
                .map((transaction) -> {

                    if (transaction.getSender().getEmail().equals(filter.getUser().getEmail())) {
                        return TransactionView.toWithdrawal(transaction);
                    }

                    return TransactionView.toDeposit(transaction);

                });

        return getPaginatedTransactions(transactions);
    }


    /**
     * Returns a paginated response from a Page
     */
    private PaginatedResponse<TransactionView> getPaginatedTransactions(Page<TransactionView> page) {
        return new PaginatedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }


    /**
     * Gets the total amount of deposits of the
     * user in the current month
     */
    public float getMonthlyDepositsTotal(User user) {
        List<Transaction> monthlyDeposits = transactionRepository.findMonthlyDepositsInRange(user,
                LocalDate.now().withDayOfMonth(1).atStartOfDay(), LocalDateTime.now());

        return monthlyDeposits.stream()
                .map(Transaction::getAmount)
                .reduce(0F, Float::sum);
    }


    /**
     * Gets the total amount of withdrawals of the
     * user in the current month
     */
    public float getMonthlyWithdrawalsTotal(User user) {
        List<Transaction> monthlyWithdrawals = transactionRepository.findMonthlyWithdrawalsInRange(user,
                LocalDate.now().withDayOfMonth(1).atStartOfDay(), LocalDateTime.now());

        return monthlyWithdrawals.stream()
                .map(Transaction::getAmount)
                .reduce(0F, Float::sum);
    }


    @Override
    public MonthlyTransactionsDTO getMonthlyTransactions(User user) {
        LocalDateTime firstDayOfTheMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime lastDayOfMonth = LocalDate.now()
                .with(TemporalAdjusters.lastDayOfMonth())
                .atTime(LocalTime.MAX);

        MonthlyTransactionsDTO monthlyTransactions = new MonthlyTransactionsDTO();

        List<TransactionView> deposits = transactionRepository.findMonthlyDepositsInRange(user, firstDayOfTheMonth,
                        lastDayOfMonth).stream().map(TransactionView::toDeposit).toList();

        List<TransactionView> withdrawals = transactionRepository.findMonthlyWithdrawalsInRange(user, firstDayOfTheMonth,
                lastDayOfMonth).stream().map(TransactionView::toWithdrawal).toList();

        monthlyTransactions.setDeposits(deposits);
        monthlyTransactions.setWithdrawals(withdrawals);

        return monthlyTransactions;
    }


    /**
     * Returns the total withdrawals for each week of the month.
     * Also returns the day ranges of each week within the WeeklyTotalsDto
     */
    public WeeklyTotalsDto getMonthWeeklyWithdrawals(User user) {
        // Sets the date ranges
        LocalDate firstDayOfTheMonth = LocalDate.now().withDayOfMonth(1);

        LocalDate lastDayOfMonth = firstDayOfTheMonth.with(TemporalAdjusters.lastDayOfMonth());

        LocalDate startOfSecondWeek = firstDayOfTheMonth.plusDays(7);

        LocalDate startOfThirdWeek = startOfSecondWeek.plusDays(7);

        LocalDate startOfFourthWeek = startOfThirdWeek.plusDays(7);


        // Converts the first week transactions and reduce them to the total
        float firstWeekTotals = transactionRepository.findMonthlyWithdrawalsInRange(user, firstDayOfTheMonth.atStartOfDay(),
                startOfSecondWeek.minusDays(1).atTime(LocalTime.MAX)).stream().map(Transaction::getAmount).reduce(0f,
                Float::sum);

        float secondWeekTotals = transactionRepository.findMonthlyWithdrawalsInRange(user, startOfSecondWeek.atStartOfDay(),
                startOfThirdWeek.minusDays(1).atTime(LocalTime.MAX)).stream().map(Transaction::getAmount).reduce(0f, Float::sum);

        float thirdWeekTotals = transactionRepository.findMonthlyWithdrawalsInRange(user, startOfThirdWeek.atStartOfDay(),
                startOfFourthWeek.minusDays(1).atTime(LocalTime.MAX)).stream().map(Transaction::getAmount).reduce(0f, Float::sum);

        float fourthWeekTotals = transactionRepository.findMonthlyWithdrawalsInRange(user, startOfFourthWeek.atStartOfDay(),
                lastDayOfMonth.atTime(LocalTime.MAX)).stream().map(Transaction::getAmount).reduce(0f, Float::sum);

        WeeklyTotalsDto weeklyTotals = new WeeklyTotalsDto();

        weeklyTotals.setTotals(List.of(firstWeekTotals, secondWeekTotals, thirdWeekTotals, fourthWeekTotals));
        weeklyTotals.setDates(List.of(firstDayOfTheMonth.atStartOfDay(), startOfSecondWeek.atStartOfDay(),
                startOfThirdWeek.atStartOfDay(), startOfFourthWeek.atStartOfDay(), lastDayOfMonth.atStartOfDay()));

        return weeklyTotals;
    }

    @Override
    @Transactional
    public FundsResponse getFunds(FundsRequest request, User user) {
        TransactionRequest fundsRequest = new TransactionRequest();
        fundsRequest.setReceiverEmail(user.getEmail());
        fundsRequest.setAmount(request.getAmount());

        createTransaction(fundsRequest, userService.getUserFromEmail("finance@vault.com").orElseThrow());

        return new FundsResponse(true);
    }

}
