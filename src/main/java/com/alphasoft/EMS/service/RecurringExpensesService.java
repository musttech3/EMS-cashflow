package com.alphasoft.EMS.service;

import com.alphasoft.EMS.model.RecurringExpenses;
import com.alphasoft.EMS.repository.RecurringExpensesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecurringExpensesService {

    RecurringExpensesRepository recurringExpensesRepository;

    @Autowired
    public RecurringExpensesService(RecurringExpensesRepository recurringExpensesRepository){
        this.recurringExpensesRepository = recurringExpensesRepository;
    }

    public RecurringExpenses findById(long id){
        return recurringExpensesRepository.findById(id).orElse(null);
    }

    public List<RecurringExpenses> findAll(){
        return recurringExpensesRepository.findAll();
    }

    @Transactional
    public void save(RecurringExpenses recurringExpenses){
        recurringExpensesRepository.save(recurringExpenses);
    }

    @Transactional
    public void deleteById(long id){
        recurringExpensesRepository.deleteById(id);
    }

}
