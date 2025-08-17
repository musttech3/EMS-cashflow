package com.alphasoft.EMS.rest;

import com.alphasoft.EMS.model.Currency;
import com.alphasoft.EMS.service.AuthenticationService;
import com.alphasoft.EMS.service.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    CurrencyService currencyService;
    AuthenticationService authenticationService;

    public CurrencyController(
            CurrencyService currencyService,
            AuthenticationService authenticationService
    ) {
        this.currencyService = currencyService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/currencies") // Test success
    public ResponseEntity<List<Currency>> getAllCurrencies(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {

        authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        List<Currency> currencies = currencyService.findAll();
        if (currencies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(currencies);
    }
    
    @PostMapping("/currencies") // Test success
    public ResponseEntity<String> addCurrency(
            @RequestBody Currency currency,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {
        authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        currencyService.save(currency);
        return ResponseEntity.ok().body("Currency added successfully");
    }
}
