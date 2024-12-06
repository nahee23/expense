package mysite.expense.controller;

import mysite.expense.dto.ExpenseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ExpenseController {

    private static List<ExpenseDTO> list = new ArrayList<>();

    static {
        ExpenseDTO e1 = new ExpenseDTO();
        e1.setName("도시가스 요금");
        e1.setDescription("우리집 가스요금");
        e1.setAmount(37000L);
        e1.setDate(new Date(System.currentTimeMillis()));
        list.add(e1);

        ExpenseDTO e2 = new ExpenseDTO();
        e2.setName("전기 요금");
        e2.setDescription("우리집 전기요금");
        e2.setAmount(27500L);
        e2.setDate(new Date(System.currentTimeMillis()));
        list.add(e2);
    }

    @GetMapping("/expenses")
    public String showList(Model model) {
        model.addAttribute("expenses", list);
        return "e_list";
    }

}
