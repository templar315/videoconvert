package com.satrumroom.controller;

import com.satrumroom.dto.FileInfoDTO;
import com.satrumroom.dto.UserDTO;
import com.satrumroom.service.FileInfoService;
import com.satrumroom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    private final FileInfoService fileInfoService;

    private void up() {
        userService.add(UserDTO.builder().login("admin").password("easypassword").role("ADMIN").build());
    }

    @GetMapping(value = "/start")
    public String start(){
        up();
        return "redirect:/";
    }

    @GetMapping(value = {"/", "/index", "/login"})
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping(value = {"/transfer"})
    public String transfer(){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDTO user = userService.findByLogin(auth.getName());
            if (user.getRole().equals("ADMIN")) return "redirect:/admin";
            else return "redirect:/user";
        } catch(Exception ex) {
            return "redirect:/logout";
        }
    }

    @GetMapping(value = "/admin")
    public String adminPanel(ModelMap model){
        model.addAttribute("users", getAll());
        return "admin";
    }

    @PostMapping(value = "/admin/registration")
    public String registration(@RequestParam String login,
                               @RequestParam String password,
                               @RequestParam String role,
                               ModelMap model){
        try {
            userService.add(UserDTO
                    .builder()
                    .login(login)
                    .password(password)
                    .role(role.isEmpty()
                            ? "USER"
                            : "ADMIN")
                    .build());
            model.addAttribute("users", getAll());
            return "redirect:/admin";
        } catch(Exception ex) {
            model.addAttribute("users", getAll());
            return "admin";
        }
    }

    @GetMapping(value = "/admin/delete/{id}")
    public String delete(@PathVariable long id, ModelMap model){
        try {
            userService.delete(id);
            model.addAttribute("users", getAll());
            return "redirect:/admin";
        } catch(Exception ex) {
            model.addAttribute("users", getAll());
            return "admin";
        }
    }

    @PostMapping(value = "/admin")
    public String getOne(@RequestParam String login, ModelMap model) {
        try {
            model.addAttribute("users", new ArrayList<>(Arrays.asList(userService.findByLogin(login))));
            return "admin";
        } catch(Exception ex) {
            model.addAttribute("users", getAll());
            return "admin";
        }
    }

    @GetMapping(value = "/admin/edit/{id}")
    public String getOne(@PathVariable long id, ModelMap model) {
        try {
            model.addAttribute("user",userService.getOne(id));
            return "edit";
        } catch(Exception ex) {
            model.addAttribute("users", getAll());
            return "admin";
        }
    }

    @PostMapping(value = "/admin/edit/{id}", params = {"login", "password"})
    public String getOne(@PathVariable long id,
                         @RequestParam String login,
                         @RequestParam String password,
                         ModelMap model) {
        try {
            UserDTO user = userService.getOne(id);
            if(login.isEmpty()) login = user.getLogin();
            if(password.isEmpty()) password = user.getPassword();
            userService.update(UserDTO
                    .builder()
                    .id(id)
                    .login(login)
                    .password(password)
                    .build());
            model.addAttribute("user",userService.getOne(id));
            return "edit";
        } catch(Exception ex) {
            model.addAttribute("users", getAll());
            return "admin";
        }
    }

    @GetMapping(value = "/admin", params = "role")
    public String findByRole(@RequestParam String role, ModelMap model) {
        try {
            model.addAttribute("users", userService.findAllByRole(role));
            return "admin";
        } catch(Exception ex) {
            model.addAttribute("users", getAll());
            return "admin";
        }
    }

    @GetMapping(value = "/user")
    public String user(ModelMap model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDTO user = userService.findByLogin(auth.getName());
            List<FileInfoDTO> files = fileInfoService.getAllById(user.getFilesInfo());
            model.addAttribute("user", user);
            model.addAttribute("files", files);
            return "user";
        } catch(Exception ex) {
            return "redirect:/logout";
        }
    }


    private List<UserDTO> getAll() {
        List<UserDTO> users = userService.getAll();
        Collections.reverse(users);
        return users;
    }

}
