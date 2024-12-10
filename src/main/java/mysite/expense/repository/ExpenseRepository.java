package mysite.expense.repository;

import mysite.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByExpenseId (String expenseId);

    List<Expense> findByNameContainingAndDateBetween(String keyword, Date start, Date end);
}
