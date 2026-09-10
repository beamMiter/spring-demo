package com.techup.spring_demo.service;

import com.techup.spring_demo.dto.LoginRequest;
import com.techup.spring_demo.dto.RegisterRequest;
import com.techup.spring_demo.entity.User;
import com.techup.spring_demo.repository.UserRepository;
import com.techup.spring_demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // สมัครสมาชิกใหม่ -> เก็บ password แบบ hash (BCrypt)
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    // เข้าสู่ระบบ -> คืน JWT (ไม่แยกข้อความ "ไม่พบ user" กับ "รหัสผิด" เพื่อความปลอดภัย)
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
