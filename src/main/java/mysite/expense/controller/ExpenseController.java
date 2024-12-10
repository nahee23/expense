package mysite.expense.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mysite.expense.dto.ExpenseDTO;
import mysite.expense.dto.ExpenseFilterDTO;
import mysite.expense.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExpenseController {

//    private static List<ExpenseDTO> list = new ArrayList<>();
//
//    static {
//        ExpenseDTO e1 = new ExpenseDTO();
//        e1.setName("도시가스 요금");
//        e1.setDescription("우리집 가스요금");
//        e1.setAmount(37000L);
//        e1.setDate(new Date(System.currentTimeMillis()));
//        list.add(e1);
//
//        ExpenseDTO e2 = new ExpenseDTO();
//        e2.setName("전기 요금");
//        e2.setDescription("우리집 전기요금");
//        e2.setAmount(27500L);
//        e2.setDate(new Date(System.currentTimeMillis()));
//        list.add(e2);
//    }

    private final ExpenseService expService;

    @GetMapping("/expenses")
    public String showList(Model model) {
        List<ExpenseDTO> list = expService.getAllExpenses();
        model.addAttribute("expenses", list);
        model.addAttribute("filter", new ExpenseFilterDTO());
        Long total = expService.totalExpenses(list);
        model.addAttribute("total", total);
        return "e_list";
    }

    //get 요청시 비용 입력을 위한 창을 보여주기
    @GetMapping("/createExpense")
    public String showCreateExpense(Model model) {
        model.addAttribute("expense", new ExpenseDTO()); //빈 expense 객체 전달
        return "e_form";
    }

    @PostMapping("/saveOrUpdateExpense")
    public String saveOrUpdateExpense(@Valid @ModelAttribute("expense") ExpenseDTO expenseDTO,
                                      BindingResult result) throws ParseException {
        System.out.println("입력한 expenseDTO 객체 : " + expenseDTO);
        if (result.hasErrors()) {
            return "e_form"; //되돌아감(validation error)
        }
        expService.saveExpenseDetails(expenseDTO);
        return "redirect:/expenses";
    }

    @GetMapping("/deleteExpense")
    public String deleteExpense(@RequestParam("id") String expenseId) {
        //@RequestParam String id 도 가능
        System.out.println("삭제 비용 번호 : " + expenseId);
        expService.deleteExpense(expenseId);
        return "redirect:/expenses";
    }

    //수정할 페이지 보여주기
    @GetMapping("/updateExpense")
    public String updateExpense(@RequestParam("id") String expenseId, Model model) {
        System.out.println("업데이트 아이템 : " + expenseId);
        //DB에서 해당 id의 객체를 전달하여 수정할 수 있게 함.
        ExpenseDTO expenseDTO = expService.getExpenseById(expenseId);
        System.out.println(expenseDTO);
        model.addAttribute("expense", expenseDTO);
        return "e_form";
    }

}
