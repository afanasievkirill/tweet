package com.example.demo.controller;

import com.example.demo.model.dto.CaptchaResponseDto;
import com.example.demo.service.UserService;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("message", "");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam ("password2") String passwordConfirmation,
                          @RequestParam ("g-recaptcha-response") String captchaResponse,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model
    ) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if(!response.isSuccess()){
            model.addAttribute("captchaError", "Fill captcha");
        }

        boolean isConirmEmpty = StringUtils.isEmpty(passwordConfirmation);
        if(isConirmEmpty){
            model.addAttribute("password2Error", "Password confirmation error!");
        }
        if(user.getPassword() != null && ! user.getPassword().equals(passwordConfirmation)){
            model.addAttribute("passwordError", "Password not equals");
        }
        if(isConirmEmpty || bindingResult.hasErrors() || !response.isSuccess()){
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exist!");
            return "registration";
        }
        return "redirect:/login";

    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActived = userService.activateUser(code);
        if(isActived){
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User activated");
        }
        else
        {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Code is not found");
        }
        return "login";
    }
}
