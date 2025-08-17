package com.alphasoft.EMS.repository;

import com.alphasoft.EMS.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
