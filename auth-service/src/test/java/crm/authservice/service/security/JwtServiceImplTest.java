package crm.authservice.service.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        userDetails = new User("testUser", "password", new ArrayList<>());
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ")); // Checks for a typical JWT structure.
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), extractedUsername);
    }

    @Test
    void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testIsTokenInvalidWithWrongUsername() {
        UserDetails anotherUser = new User("anotherUser", "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, anotherUser);
        assertFalse(isValid);
    }

    @Test
    void testExtractAllClaimsUsingReflection() throws Exception {
        String token = jwtService.generateToken(userDetails);

        Method extractAllClaimsMethod = JwtServiceImpl.class.getDeclaredMethod("extractAllClaims", String.class);
        extractAllClaimsMethod.setAccessible(true); // Bypass private access

        Claims claims = (Claims) extractAllClaimsMethod.invoke(jwtService, token);
        assertNotNull(claims);
        assertEquals(userDetails.getUsername(), claims.getSubject());
    }



}
