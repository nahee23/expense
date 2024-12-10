package mysite.expense.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mysite.expense.dto.UserDTO;
import mysite.expense.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService uService;

    //로그인 페이지
    @GetMapping({"/login","/"})
    public String showLoginPage() {
        return "login";
    }

    //회원가입 페이지
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    //유저 등록 처리
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO user,
                           BindingResult result, Model model) {
        System.out.println("유저DTO객체 : " + user);
        if (result.hasErrors()) {
            return "register";
        }
        uService.save(user);
        model.addAttribute("successMsg",true);
        return "login"; //로그인 페이지로 이동
    }

}
