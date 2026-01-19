package com.assess.docservice.controller;

import com.assess.docservice.component.TenantContext;
import com.assess.docservice.dto.LoginRequest;
import com.assess.docservice.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody LoginRequest request) {

        // USER login
        if ("user1".equals(request.getUsername())
                && "password".equals(request.getPassword())) {

            String token = jwtService.generateToken(
                    request.getUsername(),
                    List.of("ROLE_USER"),
                    "tenant1"
            );

            return ResponseEntity.ok(Map.of("accessToken", token));
        }

        // ADMIN login
        if ("admin1".equals(request.getUsername())
                && "adminpass".equals(request.getPassword())) {

            String token = jwtService.generateToken(
                    request.getUsername(),
                    List.of("ROLE_ADMIN"),
                    "tenant1"
            );

            return ResponseEntity.ok(Map.of("accessToken", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private String getTenantId() {
        return TenantContext.getTenantId();
    }


}
