package dev.olysykh.demo.services;

import dev.olysykh.demo.dto.RegistrationRequest;
import dev.olysykh.demo.entities.Role;
import dev.olysykh.demo.entities.Token;
import dev.olysykh.demo.entities.User;
import dev.olysykh.demo.repositories.RoleRepository;
import dev.olysykh.demo.repositories.TokenRepository;
import dev.olysykh.demo.repositories.UserRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    public void register(RegistrationRequest request) {
        Role userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new IllegalStateException("Role User hast not been initialized"));

        User user = User.builder()
            .firstname(request.getFirstName())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .accountLocked(false)
            .enabled(false)
            .roles(List.of(userRole))
            .build();

        userRepository.save(user);

        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) {
        var newToken = generateAndSaveActivationToken(user);
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateAndSaveActivationCode(6);

        Token token = Token.builder()
            .token(generatedToken)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .user(user)
            .build();

        tokenRepository.save(token);
        return generatedToken;


    }

    private String generateAndSaveActivationCode(int length) {

        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}