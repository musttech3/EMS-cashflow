package com.alphasoft.EMS.rest;

import com.alphasoft.EMS.dto.TransactionRequest;
import com.alphasoft.EMS.dto.UserResponse;
import com.alphasoft.EMS.exception.ResourceNotFoundException;
import com.alphasoft.EMS.model.Category;
import com.alphasoft.EMS.model.Currency;
import com.alphasoft.EMS.model.Transaction;
import com.alphasoft.EMS.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    TransactionService transactionService;
    UserService userService;
    CategoryService categoryService;
    CurrencyService currencyService;
    AuthenticationService authenticationService;

    @Autowired
    public TransactionController(
            TransactionService transactionService,
            UserService userService,
            CategoryService categoryService,
            CurrencyService currencyService,
            AuthenticationService authenticationService
    ) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.currencyService = currencyService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionRequest>> getAllTransactions(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        List<Transaction> transactions = transactionService.getAllTransactions(userResponse.getId());

        List<TransactionRequest> transactionRequests = transactions.stream()
                .map(transaction -> TransactionRequest.builder()
                        .amount(transaction.getAmount())
                        .date(transaction.getDate())
                        .desc(transaction.getDesc())
                        .currency(transaction.getCurrency().getIsoCode())
                        .category(transaction.getCategory().getCategoryName())
                        .build())
                .toList();

        return ResponseEntity.ok().body(transactionRequests);
    }


    @GetMapping("/transactions/currency/{currency}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByCurrency(
            @PathVariable String currency,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getAllTransactionsByCurrency(
                        userResponse.getId(), currency
                )
        );
    }

    @GetMapping("/transactions/category/{category}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByCategory(
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getAllTransactionsByCategory(
                        userResponse.getId(), category
                )
        );
    }

    @GetMapping("/transactions/{currency}/{category}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByCurrencyAndCategory(
            @PathVariable String currency,
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getAllTransactionsByCurrencyAndCategory(
                        userResponse.getId(), currency, category
                )
        );
    }

    @GetMapping("/transactions/{date}")
    public ResponseEntity<List<Transaction>> getTransactionsBySingleDate(
            @PathVariable String date,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        DateTimeFormatter formatter;
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        if (date.equals("today")){
            return ResponseEntity.ok().body(
                    transactionService.getTransactionsForSpecificDate(
                        userResponse.getId(), LocalDate.now()
                    )
            );
        }
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForSpecificDate(
                        userResponse.getId(), LocalDate.parse(date, formatter)
                )
        );
    }

    @GetMapping("/transactions/{date}/currency/{currency}")
    public ResponseEntity<List<Transaction>> getTransactionsBySingleDateAndCurrency(
            @PathVariable String date,
            @PathVariable String currency,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){

        DateTimeFormatter formatter;
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        if (date.equals("today")){
            return ResponseEntity.ok().body(
                    transactionService.getTransactionsForSpecificDateByCurrency(
                            userResponse.getId(), LocalDate.now(), currency
                    )
            );
        }
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForSpecificDateByCurrency(
                        userResponse.getId(), LocalDate.parse(date, formatter), currency
                )
        );
    }

    @GetMapping("/transactions/{date}/category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsBySingleDateAndCategory(
            @PathVariable String date,
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){

        DateTimeFormatter formatter;
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        if (date.equals("today")){
            return ResponseEntity.ok().body(
                    transactionService.getTransactionsForSpecificDateByCategory(
                            userResponse.getId(), LocalDate.now(), category
                    )
            );
        }
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForSpecificDateByCategory(
                        userResponse.getId(), LocalDate.parse(date, formatter), category
                )
        );
    }

    @GetMapping("/transactions/{date}/{currency}/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsBySingleDateAndCurrencyAndCategory(
            @PathVariable String date,
            @PathVariable String currency,
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){

        DateTimeFormatter formatter;
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        if (date.equals("today")){
            return ResponseEntity.ok().body(
                    transactionService.getTransactionsForSpecificDateByCurrencyAndCategory(
                            userResponse.getId(), LocalDate.now(), currency, category
                    )
            );
        }
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForSpecificDateByCurrencyAndCategory(
                        userResponse.getId(), LocalDate.parse(date, formatter), currency, category
                )
        );
    }

    @GetMapping("/transactions/last-week")
    public ResponseEntity<List<Transaction>> getTransactionsForLastWeek(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForLastWeek(
                        userResponse.getId()
                )
        );
    }

    @GetMapping("/transactions/last-week/currency/{currency}")
    public ResponseEntity<List<Transaction>> getTransactionsForLastWeekByCurrency(
            @PathVariable String currency,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForLastWeekByCurrency(
                        userResponse.getId(), currency
                )
        );
    }

    @GetMapping("/transactions/last-week/category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsForLastWeekByCategory(
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForLastWeekByCategory(
                        userResponse.getId(), category
                )
        );
    }

    @GetMapping("/transactions/last-week/{currency}/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsForLastWeekByCurrencyAndCategory(
            @PathVariable String currency,
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForLastWeekByCurrencyAndCategory(
                        userResponse.getId(), currency, category
                )
        );
    }

    @GetMapping("/transactions/current-month")
    public ResponseEntity<List<Transaction>> getTransactionsForCurrentMonth(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForCurrentMonth(
                        userResponse.getId()
                )
        );
    }

    @GetMapping("/transactions/current-month/currency/{currency}")
    public ResponseEntity<List<Transaction>> getTransactionsForCurrentMonthCurrency(
            @PathVariable String currency,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForCurrentMonthByCurrency(
                        userResponse.getId(), currency
                )
        );
    }

    @GetMapping("/transactions/current-month/category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsForCurrentMonthByCategory(
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForCurrentMonthByCategory(
                        userResponse.getId(), category
                )
        );
    }

    @GetMapping("/transactions/current-month/{currency}/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsForCurrentMonthByCurrencyAndCategory(
            @PathVariable String currency,
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForCurrentMonthByCurrencyAndCategory(
                        userResponse.getId(), currency, category
                )
        );
    }

    @GetMapping("/transactions/current-year")
    public ResponseEntity<List<Transaction>> getTransactionsForCurrentYear(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForCurrentYear(
                        userResponse.getId()
                )
        );
    }

    @GetMapping("/transactions/current-year/currency/{currency}")
    public ResponseEntity<List<Transaction>> getTransactionsForCurrentYearCurrency(
            @PathVariable String currency,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForCurrentYearByCurrency(
                        userResponse.getId(), currency
                )
        );
    }

    @GetMapping("/transactions/current-year/category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsForCurrentYearByCategory(
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForCurrentYearByCategory(
                        userResponse.getId(), category
                )
        );
    }

    @GetMapping("/transactions/current-year/{currency}/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsForCurrentYearByCurrencyAndCategory(
            @PathVariable String currency,
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForCurrentYearByCurrencyAndCategory(
                        userResponse.getId(), currency, category
                )
        );
    }

    @GetMapping("/transactions/range-time/{startDate}/{endDate}")
    public ResponseEntity<List<Transaction>> getTransactionsForRangeTime(
            @PathVariable String startDate,
            @PathVariable String endDate,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForRangeTime(
                        userResponse.getId(),
                        LocalDate.parse(startDate, formatter),
                        LocalDate.parse(endDate, formatter)
                )
        );
    }

    @GetMapping("/transactions/range-time/{startDate}/{endDate}/currency/{currency}")
    public ResponseEntity<List<Transaction>> getTransactionsForRangeTimeByCurrency(
            @PathVariable String startDate,
            @PathVariable String endDate,
            @PathVariable String currency,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForRangeTimebyCurrency(
                        userResponse.getId(),
                        LocalDate.parse(startDate, formatter),
                        LocalDate.parse(endDate, formatter),
                        currency
                )
        );
    }

    @GetMapping("/transactions/range-time/{startDate}/{endDate}/category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsForRangeTimeByCategory(
            @PathVariable String startDate,
            @PathVariable String endDate,
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForRangeTimebyCategory(
                        userResponse.getId(),
                        LocalDate.parse(startDate, formatter),
                        LocalDate.parse(endDate, formatter),
                        category
                )
        );
    }

    @GetMapping("/transactions/range-time/{startDate}/{endDate}/{currency}/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsForRangeTimeByCurrencyAndCategory(
            @PathVariable String startDate,
            @PathVariable String endDate,
            @PathVariable String currency,
            @PathVariable String category,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getTransactionsForRangeTimebyCurrencyAndCategory(
                        userResponse.getId(),
                        LocalDate.parse(startDate, formatter),
                        LocalDate.parse(endDate, formatter),
                        currency,
                        category
                )
        );
    }

    @GetMapping("/transactions/transactions-last/{number}")
    public ResponseEntity<List<Transaction>> getTransactionsForLastOrFirst(
            @PathVariable String date,
            @PathVariable String order,
            @PathVariable int number,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        return ResponseEntity.ok().body(
                transactionService.getLastOrFirstNumTransactions(
                        userResponse.getId(),
                        number
                )
        );
    }

    @GetMapping("transactions/wallet-balance/{date}")
    public float getWalletBalanceForSpecificDate(@PathVariable String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return transactionService.getSpecificDateBalance(0L, LocalDate.parse(date, formatter));
    }

    @GetMapping("transactions/wallet-balance/last-weak")
    public float getWalletBalanceForLastWeak(){
        return transactionService.getLastWeekBalance(0L);
    }

    @GetMapping("transactions/wallet-balance/current-month")
    public float getWalletBalanceForCurrentMonth(){
        return transactionService.getCurrentMonthBalance(0L);
    }

    @GetMapping("transactions/wallet-balance/current-year")
    public float getWalletBalanceForCurrentYear(){
        return transactionService.getCurrentYearBalance(0L);
    }

    @GetMapping("transactions/wallet-balance/{startDate}/{endDate}")
    public float getWalletBalanceForRangeTime(
            @PathVariable String startDate,
            @PathVariable String endDate
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return transactionService.getRangeTimeBalance(
                0L,
                LocalDate.parse(startDate, formatter),
                LocalDate.parse(endDate, formatter)
        );
    }

//    @GetMapping("transactions/wallet-balance/")

    /*
    *
    * Post methods  
    *
    * */

    @PostMapping("/transactions")
    public ResponseEntity<String> addTransaction(
            @RequestBody TransactionRequest transactionRequest,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        Transaction transaction = Transaction.builder()
                .amount(transactionRequest.getAmount())
                .date(transactionRequest.getDate())
                .desc(transactionRequest.getDesc())
                .build();

        Category category = categoryService.findCategoryByUserIdAndCategoryName(
                userResponse.getId(),
                transactionRequest.getCategory()
        );
        Currency currency = currencyService.findCurrencyByIsoCode(transactionRequest.getCurrency());

        if (currency == null) {
            throw new ResourceNotFoundException("Currency not found");
        }
        if (category == null) {
            throw new ResourceNotFoundException("Category not found");
        }

        transaction.setCategory(category);
        transaction.setCurrency(currency);
        transaction.setUser(userService.findUserById(userResponse.getId()));

        transactionService.addTransaction(transaction);
        return ResponseEntity.ok().body("Transaction added successfully");
    }




}
