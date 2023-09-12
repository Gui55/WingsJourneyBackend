package com.forthenight.wingsjourney.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.forthenight.wingsjourney.security.jwt.JwtTokenService;

@RestController
public class UserResource {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public UserResource(
        UserRepository repository, 
        AuthenticationManager authenticationManager,
        JwtTokenService jwtTokenService
    ){
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticationToken(
        @RequestBody AuthenticationRequest request
    ){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()
        );

        Authentication authentication = authenticationManager.authenticate(authToken);

        String token = jwtTokenService.generateToken(authentication);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
    
    @PostMapping("users/register")
    public ResponseEntity<User> createUser(@RequestBody User userData){
        User user = new User(
            0,
            userData.getUsername(),
            userData.getEmail(),
            userData.getPassword()
        );
        repository.save(user);
        return ResponseEntity.ok(userData);
    }

}
