package com.example.hotelapp.Controller.MainController;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Entity.User.User;
import com.example.hotelapp.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class controller {
    UserService userService;
    @Autowired
    public controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    private String home(){
        return "index";
    }
    @GetMapping("/signup")
    public String showSignupForm(Model model){
        //Model to store form data
        UserDto user = new UserDto();
        model.addAttribute("user",user);
        return "signup";
    }
    @PostMapping("/signup/save")
    public String signup(@Valid @ModelAttribute("user") UserDto userDto,
                        BindingResult result,
                         Model model
    ){
        User existingUser = userService.findUserByEmail(userDto.getEmail());
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email",null,"Oops!");
        }
        if(result.hasErrors()){
            model.addAttribute("users",userDto);
            return "signup";
        }
        userService.createUser(userDto);
        return "redirect:/signup?success";
    }
    @GetMapping("/users")
    public String listUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
