package com.forthenight.wingsjourney.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.forthenight.wingsjourney.security.jwt.JwtTokenService;

@RestController
public class UserResource {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResource(
        UserRepository repository, 
        AuthenticationManager authenticationManager,
        JwtTokenService jwtTokenService,
        BCryptPasswordEncoder passwordEncoder
    ){
        this.repository = repository;
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
            userData.getRole()
        );
        repository.save(user);
        return ResponseEntity.ok(userData);
    }

    @GetMapping("users/role")
    public Map<String,Object> getPrincipalInfo(JwtAuthenticationToken token) {
        
        Collection<String> authorities = token.getAuthorities()
          .stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());
        
        Map<String,Object> info = new HashMap<>();
        info.put("name", token.getName());
        info.put("authorities", authorities);
        info.put("tokenAttributes", token.getTokenAttributes());

        return info;
    }

    @GetMapping("games/test")
    @PreAuthorize("hasAuthority('ROLE_KING')")
    public String testLink(){
        return "A test link";
    }

}
