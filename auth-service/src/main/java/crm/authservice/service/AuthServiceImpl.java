package crm.authservice.service;

import crm.authservice.model.dto.LoginRequest;
import crm.authservice.model.dto.RegisterRequest;
import crm.authservice.model.dto.RegistrationResponse;
import crm.authservice.model.entity.AuthUser;
import crm.authservice.repository.UserRepository;
import crm.authservice.service.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public RegistrationResponse register(RegisterRequest registerDto) throws IOException {
        if (userRepository.existsAuthUserByUsername(registerDto.username())) {
            throw new BadRequestException("User exists with the username: %s".formatted(registerDto.username()));
        }

        AuthUser user = AuthUser.builder()
                .username(registerDto.username())
                .password(registerDto.password())
                .build();

        userRepository.save(user);
        return new RegistrationResponse(registerDto.username(), registerDto.password());
    }

    @Override
    public String login(LoginRequest loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));
        AuthUser user = userRepository.findByUsername(loginDto.username())
                .orElseThrow(() -> new EntityNotFoundException("User with username : %s not found".formatted(
                        loginDto.username()))
                );
        return jwtService.generateToken(user);
    }

}
