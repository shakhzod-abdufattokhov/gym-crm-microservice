package crm.authservice.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_NoAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, customUserDetailsService);
    }

    @Test
    void testDoFilterInternal_InvalidAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, customUserDetailsService);
    }



    @Test
    void testDoFilterInternal_InvalidJwt() throws ServletException, IOException {
        String token = "invalid.jwt.token";
        String username = "testUser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(null);
        when(jwtService.isTokenValid(eq(token), ArgumentMatchers.isNull())).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(token);
        verify(jwtService).isTokenValid(eq(token), ArgumentMatchers.isNull());
        verify(filterChain).doFilter(request, response);

        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }


    @Test
    void testDoFilterInternal_NoUsernameInJwt() throws ServletException, IOException {
        String token = "jwt.without.username";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn("");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(token);
        verify(filterChain).doFilter(request, response);

        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
