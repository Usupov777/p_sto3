package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.model.UserDAO;
import com.javamentor.qa.platform.security.JWT.JWTUtil;
import com.javamentor.qa.platform.security.dto.AuthenticationRequest;
import com.javamentor.qa.platform.security.dto.JWTTokenDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AuthenticationResourceController {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;

    public AuthenticationResourceController(JWTUtil jwtUtil, AuthenticationManager authenticationManager, UserDAO userDAO) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDAO = userDAO;
    }

    @PostMapping("/auth/token/")
    @ApiOperation("Возвращает строку токена в виде объекта JWTTokenDTO, на вход получает объект AuthenticationRequest, который содержит username и password")
    public ResponseEntity<JWTTokenDTO> getToken(@RequestBody AuthenticationRequest request)
    {
        JWTTokenDTO jwtTokenDTO = new JWTTokenDTO();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtTokenDTO.setToken(jwtUtil.generateAccessToken(userDAO.getUserByEmail(userDetails.getUsername()).get()));
            return new ResponseEntity<>(jwtTokenDTO, HttpStatus.OK);
        } catch (BadCredentialsException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Имя или пароль неправильны", exception);
        }
    }
}
