package com.alphasoft.EMS.repository;

import com.alphasoft.EMS.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query("SELECT c FROM Currency c")
    Optional<List<Currency>> findAllCurrencies();

    Optional<Currency> findByIsoCode(String isoCode);

}