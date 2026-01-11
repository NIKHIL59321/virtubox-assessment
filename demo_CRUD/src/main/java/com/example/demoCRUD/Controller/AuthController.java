package com.example.demoCRUD.Controller;

import com.example.demoCRUD.model.User;
import com.example.demoCRUD.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.Encoder;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder encoder;
    @PostMapping("/signup")
    public String signup(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);

        return "User Registered Successfully";
    }

}
