package travellog.demo.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import travellog.demo.auth.JwtUtil;
import travellog.demo.models.User;
import travellog.demo.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    var maybe = userRepository.findByEmail(req.getEmail());
    if (maybe.isEmpty()) return ResponseEntity.status(401).body(Map.of("error","Invalid creds"));
    User u = maybe.get();
    if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash()))
      return ResponseEntity.status(401).body(Map.of("error","Invalid creds"));
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
