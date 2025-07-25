package com.assesment.food_delivery.service;

import com.assesment.food_delivery.dto.AuthLoginRequestDto;
import com.assesment.food_delivery.dto.AuthRegisterRequestDto;
import com.assesment.food_delivery.dto.LoginResponse;
import com.assesment.food_delivery.dto.OtpRequestDto;
import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.enums.UserRole;
import com.assesment.food_delivery.repository.UserRepository;
import com.assesment.food_delivery.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.AlreadyBuiltException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtutil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(AuthLoginRequestDto request) {
        try {
            System.out.println("login api");
            System.out.println("user req ===> " + request);

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found..."));

            System.out.println("user data ===> " + user);

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid credentials...");
            }

            UserRole requestedRole;
            try {
                requestedRole = UserRole.valueOf(request.getUserRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadCredentialsException("Invalid role provided...");
            }

            if (!user.getRole().equals(requestedRole)) {
                throw new BadCredentialsException("Invalid role...");
            }

            Random random = new Random();
            int digits = 100000 + random.nextInt(900000); // ensures 6 digits (100000–999999)
            String otp = String.valueOf(digits);
            System.out.println("otp ==> "+ otp);
            user.setOtp(otp);
            userRepository.save(user);

            return user.getOtp();

        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            throw ex; // rethrow handled by exception handler or controller advice
        } catch (Exception ex) {
            throw new RuntimeException("Login failed due to unexpected error.");
        }
    }
    public LoginResponse verifyOtp(OtpRequestDto request) {
        try {
            System.out.println("otp service api");
            System.out.println("otp req ===> " + request);

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found..."));

            System.out.println("user data ===> " + user);

            String Otp = user.getOtp();

            if (Otp == null || !Otp.equals(request.getOtp())) {
                throw new RuntimeException("Invalid OTP or OTP has expired.");
            }

            String access_token = JwtUtil.GenerateToken(user.getEmail(), user.getRole());
            System.out.println("token ==> " + access_token);

            user.setOtp(null);

            return new LoginResponse(
                    "login successfully...",
                    HttpStatus.OK.value(),
                    access_token,
                    user
            );

        } catch ( RuntimeException e) {
            throw e; // rethrow handled by exception handler or controller advice
        } catch (Exception ex) {
            throw new RuntimeException("Login failed due to unexpected error.");
        }
    }

    public User signup(AuthRegisterRequestDto request) {
        try {
            System.out.println("auth service request " + request);

            if (findUserByEmail(request.getEmail()).isPresent()) {
                throw new AlreadyBuiltException("Email already exists, please login or try with a different email...");
            }

            // Password encode
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            UserRole role;
            try {
                role = UserRole.valueOf(request.getUserRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role provided.");
            }

            User user = User.builder()
                    .email(request.getEmail())
                    .password(encodedPassword)
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .role(role)
                    .username(request.getUsername())
                    .build();

            System.out.println("user builder " + user);
            return userRepository.save(user);

        } catch (AlreadyBuiltException | IllegalArgumentException ex) {
            throw ex; // will be handled by global exception handler
        } catch (Exception ex) {
            throw new RuntimeException("Signup failed due to an unexpected error.");
        }
    }


    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
