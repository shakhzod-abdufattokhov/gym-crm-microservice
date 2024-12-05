package com.gym.crm.service.client;

import com.gym.crm.model.dto.request.RegisterRequest;
import com.gym.crm.model.dto.request.TokenValidationRequest;
import com.gym.crm.model.dto.request.UserLoginRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://localhost:8222/auth")
public interface AuthClient {

    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request);

    @PostMapping("/login")
    ResponseEntity<String> login(@Valid @RequestBody UserLoginRequest request);

    @PostMapping("/validate")
    ResponseEntity<Boolean> validateToken(@RequestBody TokenValidationRequest token);

}
