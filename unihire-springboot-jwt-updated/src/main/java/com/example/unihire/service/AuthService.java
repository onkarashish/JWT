package com.example.unihire.service;

import com.example.unihire.exception.ConflictException;
import com.example.unihire.exception.NotFoundException;
import com.example.unihire.exception.UnauthorizedException;
import com.example.unihire.model.UserInfo;
import com.example.unihire.model.enums.UserRole;
import com.example.unihire.repository.UserRepository;
import com.example.unihire.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(String username, String password, String roleStr) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ConflictException("Username already exists");
        }
        UserRole role;
        try {
            role = UserRole.valueOf(roleStr.toUpperCase());
        } catch (Exception e) {
            throw new com.example.unihire.exception.BadRequestException("Invalid role");
        }
        UserInfo u = UserInfo.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(role)
                .build();
        userRepository.save(u);
        return jwtUtil.generateToken(username, role.name());
    }

    public String login(String username, String password) {
        var opt = userRepository.findByUsername(username);
        if (opt.isEmpty()) throw new UnauthorizedException("Invalid credentials");
        UserInfo u = opt.get();
        if (!passwordEncoder.matches(password, u.getPassword())) throw new UnauthorizedException("Invalid credentials");
        return jwtUtil.generateToken(u.getUsername(), u.getRoles().name());
    }

    public UserInfo findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
