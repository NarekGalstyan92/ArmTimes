package com.armtimes.armtimes.controller;


import com.armtimes.armtimes.entity.Role;
import com.armtimes.armtimes.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){
        return "accessDenied";
    }

//    @GetMapping("/loginSuccess")
//    public String loginSuccess(@AuthenticationPrincipal CurrentUser currentUser){
//        if (currentUser != null){
//            User user = currentUser.getUser();
//            if (user.getRole() == Role.ADMIN) {
//                return "redirect:/admin";
//            } else if (user.getRole() == Role.USER) {
//                return "redirect:/user";
//            }
//        }
//        return "redirect:/";
//    }
    @GetMapping("/loginPage")
    public String loginPage(@RequestParam(value = "error", required = false) String error, ModelMap modelMap){
        if (error != null && error.equals("true")){
            modelMap.addAttribute("error", "true");
        }
        return "loginPage";
    }
}
