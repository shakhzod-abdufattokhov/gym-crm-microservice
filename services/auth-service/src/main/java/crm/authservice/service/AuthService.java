package crm.authservice.service;

import crm.authservice.model.dto.LoginRequest;
import crm.authservice.model.dto.RegisterRequest;
import crm.authservice.model.dto.RegistrationResponse;

import java.io.IOException;

public interface AuthService {

    RegistrationResponse register(RegisterRequest registerDto) throws IOException;

    String login(LoginRequest loginDto);

}
