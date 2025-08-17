package com.alphasoft.EMS.repository;

import com.alphasoft.EMS.model.RecurringExpenses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringExpensesRepository extends JpaRepository<RecurringExpenses, Long> {
}