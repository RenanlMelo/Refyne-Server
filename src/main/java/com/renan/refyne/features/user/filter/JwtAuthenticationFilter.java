package com.renan.refyne.features.user.filter;

import com.renan.refyne.features.auth.security.JwtClaimsExtractor;
import com.renan.refyne.features.auth.security.JwtValidator;
import com.renan.refyne.features.user.entity.User;
import com.renan.refyne.features.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
  extends OncePerRequestFilter {

  private final UserRepository userRepository;

  private final JwtClaimsExtractor claimsExtractor;

  private final JwtValidator jwtValidator;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {

    String authHeader =
      request.getHeader("Authorization");

    if (
      authHeader == null ||
        !authHeader.startsWith("Bearer ")
    ) {

      filterChain.doFilter(request, response);

      return;
    }

    String token = authHeader.substring(7);

    String email =
      claimsExtractor.extractUsername(token);

    if (
      email != null &&
        SecurityContextHolder
          .getContext()
          .getAuthentication() == null
    ) {

      User user =
        userRepository.findByEmail(email)
          .orElse(null);

      if (
        user != null &&
          jwtValidator.isValid(token, user)
      ) {

        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
            user,
            null,
            user.getAuthorities()
          );

        SecurityContextHolder
          .getContext()
          .setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
