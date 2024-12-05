package com.gym.crm.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserLoginRequest(
        @NotNull(message = "Username can not be null")
        @NotBlank(message = "Username can not be blank")
        String username,

        @NotNull(message = "Password can not be null")
        @NotBlank(message = "Password can not be blank")
        String password
) {}
