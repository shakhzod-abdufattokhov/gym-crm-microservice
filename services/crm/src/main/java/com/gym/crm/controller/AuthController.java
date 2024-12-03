package com.gym.crm.controller;

import com.gym.crm.controller.documentation.AuthControllerDocumentation;
import com.gym.crm.model.dto.request.TraineeRequest;
import com.gym.crm.model.dto.request.TrainerRequest;
import com.gym.crm.model.dto.request.UserLoginRequest;
import com.gym.crm.model.dto.response.ApiResponse;
import com.gym.crm.model.dto.response.RegistrationResponse;
import com.gym.crm.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocumentation {

    private final AuthService authService;

    @PostMapping("/register-trainee")
    public ApiResponse<RegistrationResponse> register(@RequestBody @Valid TraineeRequest dto) throws IOException {
        log.info("Registering trainee with the request : {}", dto);
        RegistrationResponse registrationResponse = authService.register(dto);
        return new ApiResponse<>(201, true, registrationResponse, "Saved successfully!");
    }

    @PostMapping("/register-trainer")
    public ApiResponse<RegistrationResponse> register(@RequestBody @Valid TrainerRequest dto) throws IOException {
        log.info("Registering trainer with the request : {}", dto);
        RegistrationResponse registrationResponse = authService.register(dto);
        return new ApiResponse<>(201, true,   registrationResponse, "Saved successfully!");
    }

    @GetMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody UserLoginRequest dto) {
        log.info("Logging in with username : {}", dto.username());
        String login = authService.login(dto);
        return new ApiResponse<>(200, true, login, "OK");
    }

}
