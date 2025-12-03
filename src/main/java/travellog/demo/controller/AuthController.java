package travellog.demo.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import travellog.demo.auth.JwtUtil;
import travellog.demo.models.User;
import travellog.demo.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

      @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
    if (userRepository.findByEmail(req.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
    }
    User u = User.builder()
      .email(req.getEmail())
      .passwordHash(passwordEncoder.encode(req.getPassword()))
      .displayName(req.getDisplayName())
      .createdAt(Instant.now().toEpochMilli())
      .build();
    userRepository.save(u);
    String token = jwtUtil.generateToken(u.getId());
    return ResponseEntity.ok(Map.of("token", token));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    log.info("Login attempt for email: {}", req.getEmail());
    var maybe = userRepository.findByEmail(req.getEmail());
    if (maybe.isEmpty()) {
      log.warn("User not found: {}", req.getEmail());
      return ResponseEntity.status(401).body(Map.of("error","Invalid credentials"));
    }
    User u = maybe.get();
    log.info("User found, checking password...");
    if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
      log.warn("Password mismatch for user: {}", req.getEmail());
      return ResponseEntity.status(401).body(Map.of("error","Invalid credentials"));
    }
    log.info("Login successful for user: {}", req.getEmail());
    String token = jwtUtil.generateToken(u.getId());
    return ResponseEntity.ok(Map.of("token", token));
  }

  @Data static class RegisterRequest {
    private String email;
    private String password;
    private String displayName;
  }
  @Data static class LoginRequest {
    private String email;
    private String password;
  }

    
}
