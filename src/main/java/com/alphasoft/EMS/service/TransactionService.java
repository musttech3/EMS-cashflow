package com.alphasoft.EMS.service;

import com.alphasoft.EMS.model.Transaction;
import com.alphasoft.EMS.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public Transaction findById(long id){
        return transactionRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Transaction transaction){
        transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteById(long id){
        transactionRepository.deleteById(id);
    }

    public List<Transaction> getAllTransactions(Long userId){
        return transactionRepository.findAllTransactions(userId);
    }

    public List<Transaction> getAllTransactionsByCurrency(Long userId, String currency){

        return transactionRepository.findAllTransactionsByCurrency(currency, userId);
    }

    public List<Transaction> getAllTransactionsByCategory(Long userId, String category){

        return transactionRepository.findAllTransactionsByCategoryId(category, userId);
    }

    public List<Transaction> getAllTransactionsByCurrencyAndCategory(Long userId,  String currency, String category){

        return transactionRepository.findAllTransactionsByCurrencyAndCategoryId(currency, category, userId);
    }

    public List<Transaction> getTransactionsForLastWeek(Long userId){

        return transactionRepository.findTransactionsByRangeDate(LocalDate.now().minusWeeks(1), LocalDate.now(), userId);
    }

    public List<Transaction> getTransactionsForLastWeekByCurrency(Long userId, String currecy){

        return transactionRepository.findTransactionsByRangeDateAndCurrency(LocalDate.now().minusWeeks(1), LocalDate.now(),currecy , userId);
    }

    public List<Transaction> getTransactionsForLastWeekByCategory(Long userId, String category){

        return transactionRepository.findTransactionsByRangeDateAndCategoryId(LocalDate.now().minusWeeks(1), LocalDate.now(), category, userId);
    }

    public List<Transaction> getTransactionsForLastWeekByCurrencyAndCategory(Long userId, String currency, String category){

        return transactionRepository.findTransactionsByRangeDateAndCurrencyAndCategoryId(LocalDate.now().minusWeeks(1), LocalDate.now(),currency , category, userId);
    }

    public List<Transaction> getTransactionsForCurrentMonth(Long userId){

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return transactionRepository.findTransactionsByRangeDate(startDate, endDate, userId);
    }

    public List<Transaction> getTransactionsForCurrentMonthByCurrency(Long userId, String currency){

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return transactionRepository.findTransactionsByRangeDateAndCurrency(startDate, endDate,currency , userId);
    }

    public List<Transaction> getTransactionsForCurrentMonthByCategory(Long userId, String category){

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return transactionRepository.findTransactionsByRangeDateAndCategoryId(startDate, endDate, category, userId);
    }

    public List<Transaction> getTransactionsForCurrentMonthByCurrencyAndCategory(Long userId, String currency, String category){

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return transactionRepository.findTransactionsByRangeDateAndCurrencyAndCategoryId(startDate, endDate,currency , category, userId);
    }

    public List<Transaction> getTransactionsForCurrentYear(Long userId){

        LocalDate startDate = LocalDate.now().withDayOfYear(1);
        LocalDate endDate = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
        return transactionRepository.findTransactionsByRangeDate(startDate, endDate, userId);
    }

    public List<Transaction> getTransactionsForCurrentYearByCurrency(Long userId, String currency){

        LocalDate startDate = LocalDate.now().withDayOfYear(1);
        LocalDate endDate = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
        return transactionRepository.findTransactionsByRangeDateAndCurrency(startDate, endDate, currency, userId);
    }

    public List<Transaction> getTransactionsForCurrentYearByCategory(Long userId, String category){

        LocalDate startDate = LocalDate.now().withDayOfYear(1);
        LocalDate endDate = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
        return transactionRepository.findTransactionsByRangeDateAndCategoryId(startDate, endDate,category , userId);
    }

    public List<Transaction> getTransactionsForCurrentYearByCurrencyAndCategory(Long userId, String currency, String category){

        LocalDate startDate = LocalDate.now().withDayOfYear(1);
        LocalDate endDate = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
        return transactionRepository.findTransactionsByRangeDateAndCurrencyAndCategoryId(startDate, endDate, currency, category, userId);
    }

    public List<Transaction> getTransactionsForRangeTime(Long userId, LocalDate startDate, LocalDate endDate){

        return transactionRepository.findTransactionsByRangeDate(startDate, endDate, userId);
    }

    public List<Transaction> getTransactionsForRangeTimebyCurrency(Long userId, LocalDate startDate, LocalDate endDate, String currency){

        return transactionRepository.findTransactionsByRangeDateAndCurrency(startDate, endDate, currency, userId);
    }

    public List<Transaction> getTransactionsForRangeTimebyCategory(Long userId, LocalDate startDate, LocalDate endDate, String category){

        return transactionRepository.findTransactionsByRangeDateAndCategoryId(startDate, endDate, category, userId);
    }

    public List<Transaction> getTransactionsForRangeTimebyCurrencyAndCategory(Long userId, LocalDate startDate, LocalDate endDate, String currency, String category){

        return transactionRepository.findTransactionsByRangeDateAndCurrencyAndCategoryId(startDate, endDate, currency, category, userId);
    }

    public List<Transaction> getTransactionsForSpecificDate(Long userId, LocalDate date){

        return transactionRepository.findTransactionsBySingleDate(date, userId);
    }

    public List<Transaction> getTransactionsForSpecificDateByCurrency(Long userId, LocalDate date, String currency){

        return transactionRepository.findTransactionsBySingleDateAndCurrency(date, currency, userId);
    }

    public List<Transaction> getTransactionsForSpecificDateByCategory(Long userId, LocalDate date, String category){

        return transactionRepository.findTransactionsBySingleDateAndCategoryId(date, category, userId);
    }

    public List<Transaction> getTransactionsForSpecificDateByCurrencyAndCategory(Long userId, LocalDate date, String currency, String category){

        return transactionRepository.findTransactionsBySingleDateAndCurrencyAndCategoryId(date, currency, category, userId);
    }

    public List<Transaction> getLastOrFirstNumTransactions(Long userId, int num) {
        Pageable pageable = PageRequest.of(0, num);
        return transactionRepository.findLastNumTransactions(userId, pageable);
    }



    /*
    *
    *  Fot Getting Total Income or Expense and Balance
    *
    * */

    public float getTotalForAllIncomeOrExpense(Long userId, String type, String range){

        if (range.equals("all")){
            return transactionRepository.findTotalAmountForAll(type,userId);        }

        if (!(type.equals("INCOME"))  && !(type.equals("EXPENSE"))){
            return 0000f;
        }

        return transactionRepository.findTotalAmountForAll(type, userId);
    }

    public float getTotalForAllIncomeOrExpenseByCurrency(Long userId, String type, String currency){

        if (!(type.equals("INCOME"))  && !(type.equals("EXPENSE"))){
            return 0000f;
        }

        return transactionRepository.findTotalAmountForAllByCurrency(type, currency, userId);
    }

    public float getTotalLastWeekIncomeOrExpense(Long userId, String type){

        if ( !(type.equals("INCOME")) && !(type.equals("EXPENSE")) ){
            return 0000f;
        }

        return transactionRepository.findTotalAmountByDateRange(
                LocalDate.now().minusWeeks(1),
                LocalDate.now(),
                type,
                userId
        );

    }

    public float getTotalLastWeekIncomeOrExpenseByCurrency(Long userId, String type, String currency){

        if ( !(type.equals("INCOME")) && !(type.equals("EXPENSE")) ){
            return 0000f;
        }

        return transactionRepository.findTotalAmountByDateRangeAndCurrency(
                LocalDate.now().minusWeeks(1),
                LocalDate.now(),
                type,
                currency,
                userId
        );

    }

    public float getTotalCurrentMonthIncomeOrExpense(Long userId, String type){

        if ( !(type.equals("INCOME")) && !(type.equals("EXPENSE")) ){
            return 0000f;
        }

        return transactionRepository.findTotalAmountByDateRange(
                LocalDate.now().withDayOfMonth(1),
                LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()),
                type,
                userId
        );

    }

    public float getTotalCurrentYearIncomeOrExpense(Long userId, String type){

        if ( !(type.equals("INCOME")) && !(type.equals("EXPENSE")) ){
            return 0000f;
        }

        return transactionRepository.findTotalAmountByDateRange(
                LocalDate.now().withDayOfYear(1),
                LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear()),
                type,
                userId
        );

    }

    public float getTotalSpecificDateIncomeOrExpense(Long userId, LocalDate date, String type){

        if ( !(type.equals("INCOME")) && !(type.equals("EXPENSE")) ){
            return 0000f;
        }

        return transactionRepository.findTotalAmountBySpecificDate(date, type, userId);

    }

    public float getTotalRangeTimeIncomeOrExpense(Long userId, LocalDate startTime, LocalDate endTime, String type){

        if ( !(type.equals("INCOME")) && !(type.equals("EXPENSE")) ){
            return 0000f;
        }

        return transactionRepository.findTotalAmountByDateRange(
                startTime,
                endTime,
                type,
                userId
        );

    }


    public float getLastWeekBalance(Long userId){
        return getTotalLastWeekIncomeOrExpense(userId, "INCOME") - getTotalLastWeekIncomeOrExpense(userId, "EXPENSE");
    }

    public float getCurrentMonthBalance(Long userId){
        return getTotalCurrentMonthIncomeOrExpense(userId, "INCOME") - getTotalCurrentMonthIncomeOrExpense(userId, "EXPENSE");
    }

    public float getCurrentYearBalance(Long userId){
        return getTotalCurrentYearIncomeOrExpense(userId, "INCOME") - getTotalCurrentYearIncomeOrExpense(userId, "EXPENSE");
    }

    public float getSpecificDateBalance(Long userId, LocalDate date){
        return getTotalSpecificDateIncomeOrExpense(userId, date, "INCOME") - getTotalSpecificDateIncomeOrExpense(userId, date, "EXPENSE");
    }

    public float getRangeTimeBalance(Long userId, LocalDate startDate, LocalDate endDate){
        return getTotalRangeTimeIncomeOrExpense(userId, startDate, endDate, "INCOME") - getTotalRangeTimeIncomeOrExpense(userId, startDate, endDate, "EXPENSE");
    }


    /*
    *
    * Post Methods
    *
    * */


    public void addTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    /*
    *
    * Put Methods
    *
    * */

    public void updateTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    /*
    *
    * Delete Methods
    *
    * */

    public void deleteTransaction(Transaction transaction){
        transactionRepository.delete(transaction);
    }

}
