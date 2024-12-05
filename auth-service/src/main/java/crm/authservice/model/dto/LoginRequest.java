package crm.authservice.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Username can not be blank")
        String username,

        @NotBlank(message = "Password can not be blank")
        String password
) {}
