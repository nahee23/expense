package mysite.expense.service;

import lombok.RequiredArgsConstructor;
import mysite.expense.dto.ExpenseDTO;
import mysite.expense.entity.Expense;
import mysite.expense.repository.ExpenseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //final 붙은 필드로 생성자를 만듬
public class ExpenseService {

    private final ExpenseRepository expRepo;
    private final ModelMapper modelMapper;

//    //autowire 대신에 생성자 주입
//    public ExpenseService(ExpenseRepository expRepo) {
//        this.expRepo = expRepo;
//    }

    //모든 비용 리스트를 가져옴
    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> list = expRepo.findAll();
        List<ExpenseDTO> listDTO = list.stream() //스트림으로 변환
                .map(this::mapToDTO)            //mapToDTO로 모두 변환됨, 람다식 : .map(Expense e -> mapToDTO(e))
                .collect(Collectors.toList()); //다시 리스트로
        return listDTO;
    }

    //엔티티 => DTO 변환 (값을 전달)
    public ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
//        ExpenseDTO expenseDTO = new ExpenseDTO(); //빈 객체
//        expenseDTO.setId(expense.getId());
//        expenseDTO.setExpenseId(expense.getExpenseId());
//        expenseDTO.setName(expense.getName());
//        expenseDTO.setDescription(expense.getDescription());
//        expenseDTO.setAmount(expense.getAmount());
//        expenseDTO.setDate(expense.getDate());
        return expenseDTO;

    }

}
