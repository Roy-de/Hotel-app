package com.example.hotelapplication.Controller.Public;

import com.example.hotelapplication.DAO.UserAccountDao;
import com.example.hotelapplication.Entity.UserEntity;
import com.example.hotelapplication.Service.User.UserAccountService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class LoginAndSignup {
    private UserAccountService userAccountService;
    @GetMapping("/signup")
    public String showRegistrationForm(Model model){
        //Model to store form data
        UserAccountDao userAccountDao = new UserAccountDao();
        model.addAttribute("user",userAccountDao);
        return "signup";
    }
    @PostMapping("/signup/save")
    public String signup(
            @ModelAttribute("user")UserAccountDao userAccountDao,
            BindingResult result,
            Model model
    ){
        UserEntity existingUser = userAccountService.findUserByUsername(userAccountDao.getUsername());
        //Check if there is an existing account
        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("username",null,"There exists an account with that username");
        }
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email",null,"there exists an account with that email");
        }
        if(result.hasErrors()){
            model.addAttribute("user",userAccountDao);
            return "/signup";
        }
        userAccountService.SaveUser(userAccountDao);
        return  "redirect:/signup?success";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
