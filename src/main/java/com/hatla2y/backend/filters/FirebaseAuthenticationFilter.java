package com.hatla2y.backend.filters;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.hatla2y.backend.models.User;
import com.hatla2y.backend.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final Set<AntPathRequestMatcher> pathMatchers= new HashSet<>();


    public FirebaseAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addExceptionMatcher(AntPathRequestMatcher matcher) {
        pathMatchers.add(matcher);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        for (AntPathRequestMatcher matcher : pathMatchers) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Missing or invalid Authorization token");
            return;
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);
            String email = decodedToken.getEmail();
            if (email != null) {
                User user;
                if (userRepository.existsUserByEmail(email)) {
                    user = userRepository.getUserByEmail(email);

                } else {
                    String userName = decodedToken.getName();
                    user = new User();
                    user.setEmail(email);
                    user.setFullName(userName);
                    user = userRepository.save(user);
                }
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (FirebaseAuthException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(e.getMessage());
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
