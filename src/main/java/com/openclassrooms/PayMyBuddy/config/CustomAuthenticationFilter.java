package com.openclassrooms.PayMyBuddy.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        System.out.println("Tentative de connexion avec Email System: " + email);
        logger.debug("Tentative de connexion avec Email: {}", email);
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        logger.debug("Connexion réussie pour l'utilisateur : {}", authResult.getName());
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        logger.debug("Échec de la connexion : {}", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }
}

