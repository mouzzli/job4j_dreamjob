package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static ru.job4j.dreamjob.util.Session.setSession;

@ThreadSafe
@Controller
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/formAddUser")
    public String addUser(Model model, HttpSession session, @RequestParam(name = "fail", required = false) Boolean fail) {
        setSession(model, session);
        model.addAttribute("fail", fail != null);
        return "addUser";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user) {
        Optional<User> regUser = service.addUSer(user);
        if (regUser.isEmpty()) {
            return "redirect:/formAddUser?fail=true";
        }
        return "redirect:/index";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, HttpSession session, @RequestParam(name = "fail", required = false) Boolean fail) {
        setSession(model, session);
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = service.findUserByEmailAndPassword(
                user.getEmail(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/index";
    }
}
