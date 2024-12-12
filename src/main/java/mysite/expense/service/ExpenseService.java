package mysite.expense.service;

import lombok.RequiredArgsConstructor;
import mysite.expense.dto.ExpenseDTO;
import mysite.expense.dto.ExpenseFilterDTO;
import mysite.expense.entity.Expense;
import mysite.expense.entity.User;
import mysite.expense.exception.ExpenseNotFoundException;
import mysite.expense.repository.ExpenseRepository;
import mysite.expense.util.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //final 붙은 필드로 생성자를 만듬
public class ExpenseService {

    private final ExpenseRepository expRepo;
    private final UserService uService;
    private final ModelMapper modelMapper;

//    //autowire 대신에 생성자 주입
//    public ExpenseService(ExpenseRepository expRepo) {
//        this.expRepo = expRepo;
//    }

    //모든 비용 리스트를 가져옴 (인증 유저의 비용들만)
    public List<ExpenseDTO> getAllExpenses() {
        User user = uService.getLoggedUser();
        List<Expense> list = expRepo.findByUserIdAndDateBetween(
                user.getId(),
                Date.valueOf(LocalDate.now().withDayOfMonth(1)),
                Date.valueOf(LocalDate.now()));
        List<ExpenseDTO> listDTO = list.stream() //스트림으로 변환
                .map(this::mapToDTO)            //mapToDTO로 모두 변환됨, 람다식 : .map(Expense e -> mapToDTO(e))
                .collect(Collectors.toList()); //다시 리스트로
        return listDTO;
    }

    //엔티티 => DTO 변환 (값을 전달)
    public ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
        expenseDTO.setDateString(DateTimeUtil.convertDateString(expense.getDate()));
//        ExpenseDTO expenseDTO = new ExpenseDTO(); //빈 객체
//        expenseDTO.setId(expense.getId());
//        expenseDTO.setExpenseId(expense.getExpenseId());
//        expenseDTO.setName(expense.getName());
//        expenseDTO.setDescription(expense.getDescription());
//        expenseDTO.setAmount(expense.getAmount());
//        expenseDTO.setDate(expense.getDate());
        return expenseDTO;

    }

    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) throws ParseException {
        //1. DTO => Entity
        Expense expense = mapToEntity(expenseDTO);
        expense.setUser(uService.getLoggedUser());
        //2. DB에 저장
        expense = expRepo.save(expense);
        //3. Entity => DTO
        return mapToDTO(expense);
    }

    private Expense mapToEntity(ExpenseDTO expenseDTO) throws ParseException {

        Expense expense = modelMapper.map(expenseDTO, Expense.class);
        //1. expenseId 입력 ( 유니크 문자열 자동생성 ), 업데이트일 경우 id를 만들지 않는다.
        if(expenseDTO.getId() == null) {
            expense.setExpenseId(UUID.randomUUID().toString());
        }
        //2. date 입력 ("2024-12-17") => sql Date)
        expense.setDate(DateTimeUtil.convertStringToDate(expenseDTO.getDateString()));
        return expense;
    }

    //비용 id 로 삭제하기
    public void deleteExpense(String id) {
        Expense expense = expRepo.findByExpenseId(id).orElseThrow(()->
                //못 찾을 경우
                new ExpenseNotFoundException("해당 ID의 비용을 찾을 수 없습니다."));
        expRepo.delete(expense);
    }

    //expenseId 로 수정할 expense 찾아서 expenseDTO 변환하여 리턴
    public ExpenseDTO getExpenseById(String id) {
        Expense expense = expRepo.findByExpenseId(id).orElseThrow(
                ()-> new ExpenseNotFoundException("해당 ID의 비용을 찾을 수 없습니다.")
        );
        return mapToDTO(expense); //DTO 변환
    }

    public List<ExpenseDTO> getFilterExpenses(ExpenseFilterDTO filter) throws ParseException {
        String keyword = filter.getKeyword();
        String sortBy = filter.getSortBy();
        String startDate = filter.getStartDate();
        String endDate = filter.getEndDate();
        //sql 날짜로 변경
        Date startDay = !startDate.isEmpty() ? DateTimeUtil.convertStringToDate(startDate) : new Date(0);
        Date endDay = !endDate.isEmpty() ? DateTimeUtil.convertStringToDate(endDate) : new Date(System.currentTimeMillis());

        User user = uService.getLoggedUser();
        List<Expense> list = expRepo.findByNameContainingAndDateBetweenAndUserId(keyword,startDay,endDay,user.getId());
        List<ExpenseDTO> filterlist = list.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
                //날짜 또는 가격으로 정렬
                if(sortBy.equals("date")) {
                    filterlist.sort(((o1,o2)-> o2.getDate().compareTo(o1.getDate())));
                } else {
                    filterlist.sort(((o1,o2)-> o2.getAmount().compareTo(o1.getAmount())));
                }
        return filterlist;
    }

    //리스트의 총비용을 합계
    public Long totalExpenses(List<ExpenseDTO> expenses) {
        Long sum = expenses.stream().mapToLong(ExpenseDTO::getAmount).sum();
//                .map(x->x.getAmount())
//                .reduce(0L, Long::sum);
        // 원래 아이템이 -> 다음에 있는 아이템으로 바뀜 (리스트를 Long 리스트로 바꿈?)
        return sum;
    }

}
