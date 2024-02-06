package com.forthenight.wingsjourney.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.forthenight.wingsjourney.security.jwt.JwtTokenService;

@RestController
public class UserResource {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResource(
        UserRepository repository, 
        RoleRepository roleRepository,
        AuthenticationManager authenticationManager,
        JwtTokenService jwtTokenService,
        BCryptPasswordEncoder passwordEncoder
    ){
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
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
            passwordEncoder.encode(userData.getPassword()),
            getDefaultRole()
        );
        repository.save(user);
        return ResponseEntity.ok(userData);
    }

    private Set<Role> getDefaultRole(){
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("KING"));
        return roles;
    }

}
