package com.alphasoft.EMS.service;

import com.alphasoft.EMS.model.Budget;
import com.alphasoft.EMS.repository.BudgetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BudgetService {

    BudgetRepository budgetRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository){
        this.budgetRepository = budgetRepository;
    }

    public Budget findById(long id){
        return budgetRepository.findById(id).orElse(null);
    }

    public List<Budget> findAll(){
        return budgetRepository.findAll();
    }

    @Transactional
    public void save(Budget budget){
        budgetRepository.save(budget);
    }

    @Transactional
    public void deleteById(long id){
        budgetRepository.deleteById(id);
    }

}
