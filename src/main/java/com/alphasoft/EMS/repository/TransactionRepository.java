package com.alphasoft.EMS.repository;

import com.alphasoft.EMS.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
//@EnableJpaRepositories
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // For All transactions by User
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.id DESC")
    List<Transaction> findAllTransactions(@Param("userId") Long userId);

    // For All transactions by Currency and User
    @Query("SELECT t FROM Transaction t WHERE t.currency = :currency AND t.user.id = :userId")
    List<Transaction> findAllTransactionsByCurrency(
            @Param("currency") String currency,
            @Param("userId") Long userId
    );

    // For All transactions by Category and User
    @Query("SELECT t FROM Transaction t WHERE t.category.categoryName = :categoryName AND t.user.id = :userId")
    List<Transaction> findAllTransactionsByCategoryId(
            @Param("categoryName") String categoryName,
            @Param("userId") Long userId
    );

    // For All transactions by Currency and Category and User
    @Query("SELECT t FROM Transaction t WHERE t.currency = :currency AND t.category.categoryName = :categoryName AND t.user.id = :userId")
    List<Transaction> findAllTransactionsByCurrencyAndCategoryId(
            @Param("currency") String currency,
            @Param("categoryName") String categoryName,
            @Param("userId") Long userId
    );


    // For specific Month or Year or "Range of Time" by User
    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.user.id = :userId")
    List<Transaction> findTransactionsByRangeDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("userId") Long userId
    );

    // For specific Month or Year or "Range of Time" by Currency and User
    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.currency = :currency AND t.user.id = :userId")
    List<Transaction> findTransactionsByRangeDateAndCurrency(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("currency") String currency,
            @Param("userId") Long userId
    );

    // For specific Month or Year or "Range of Time" by Category and User
    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.category.categoryName = :categoryName AND t.user.id = :userId")
    List<Transaction> findTransactionsByRangeDateAndCategoryId(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryName") String categoryName,
            @Param("userId") Long userId
    );

    // For specific Month or Year or "Range of Time" by Currency and Category and User
    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.currency = :currency AND t.category.categoryName = :categoryName AND t.user.id = :userId")
    List<Transaction> findTransactionsByRangeDateAndCurrencyAndCategoryId(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("currency") String currency,
            @Param("categoryName") String categoryName,
            @Param("userId") Long userId
    );

    // For specific date or today by User
    @Query("SELECT t FROM Transaction t WHERE t.date = :date AND t.user.id = :userId")
    List<Transaction> findTransactionsBySingleDate(
            @Param("date") LocalDate date,
            @Param("userId") Long userId
    );

    // For specific date or today by Currency and User
    @Query("SELECT t FROM Transaction t WHERE t.date = :date AND t.currency = :currency AND t.user.id = :userId")
    List<Transaction> findTransactionsBySingleDateAndCurrency(
            @Param("date") LocalDate date,
            @Param("currency") String currency,
            @Param("userId") Long userId
    );

    // For specific date or today by Category and User
    @Query("SELECT t FROM Transaction t WHERE t.date = :date AND t.category.categoryName = :categoryName AND t.user.id = :userId")
    List<Transaction> findTransactionsBySingleDateAndCategoryId(
            @Param("date") LocalDate date,
            @Param("categoryName") String categoryName,
            @Param("userId") Long userId
    );

    // For specific date or today by Currency and Category and User
    @Query("SELECT t FROM Transaction t WHERE t.date = :date AND t.currency = :currency AND t.category.categoryName = :categoryName AND t.user.id = :userId")
    List<Transaction> findTransactionsBySingleDateAndCurrencyAndCategoryId(
            @Param("date") LocalDate date,
            @Param("currency") String currency,
            @Param("categoryName") String  categoryName,
            @Param("userId") Long userId
    );

    // For Last 5 Transactions for today by User
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.id DESC")
    List<Transaction> findLastNumTransactions(
            @Param("userId") Long userId,
            Pageable pageable
    );

    // Amount of Income or Expense for all Transactions by user
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.category.type = :type AND t.user.id = :userId")
    float findTotalAmountForAll(
            @Param("type") String type,
            @Param("userId") Long userId
    );

    // Amount of Income or Expense for all Transactions by Currency and User
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.category.type = :type AND currency = :currency AND t.user.id = :userId")
    float findTotalAmountForAllByCurrency(
            @Param("type") String type,
            @Param("currency") String currency,
            @Param("userId") Long userId
    );

    // Amount of Income or Expense for specific Month or Year or "Range of Time" and User
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.category.type = :type AND t.user.id = :userId")
    float findTotalAmountByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("type") String type,
            @Param("userId") Long userId
    );

    // Amount of Income or Expense by Currency for specific Month or Year or "Range of Time" and User
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.category.type = :type AND currency = :currency AND t.user.id = :userId")
    float findTotalAmountByDateRangeAndCurrency(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("type") String type,
            @Param("currency") String currency,
            @Param("userId") Long userId
    );

    // Amount of Income or Expense for specific Date or today and User
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.date = :date AND t.category.type = :type AND t.user.id = :userId")
    float findTotalAmountBySpecificDate(
            @Param("date") LocalDate date,
            @Param("type") String type,
            @Param("userId") Long userId
    );

    // Amount of Income or Expense by Currency for specific Date or today and User
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.date = :date AND t.category.type = :type AND currency = :currency AND t.user.id = :userId")
    float findTotalAmountBySpecificDateAndCurrency(
            @Param("date") LocalDate date,
            @Param("type") String type,
            @Param("currency") String currency,
            @Param("userId") Long userId
    );



//    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")

}
