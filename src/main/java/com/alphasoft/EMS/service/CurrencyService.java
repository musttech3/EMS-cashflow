package com.alphasoft.EMS.service;

import com.alphasoft.EMS.model.Currency;
import com.alphasoft.EMS.repository.CurrencyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public Currency findById(long id){
        return currencyRepository.findById(id).orElse(null);
    }

    public List<Currency> findAll(){
        return currencyRepository.findAllCurrencies().orElse(null);
    }

    @Transactional
    public void save(Currency currency){
        currencyRepository.save(currency);
    }

    @Transactional
    public void deleteById(long id){
        currencyRepository.deleteById(id);
    }


    public Currency findCurrencyByIsoCode(String isoCode) {
        return currencyRepository.findByIsoCode(isoCode).orElse(null);
    }
}
