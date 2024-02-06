package com.forthenight.wingsjourney.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.forthenight.wingsjourney.user.CustomUserDetailsService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

	private static final String[] WHITE_LIST = {
		"authenticate",
		"users/register"
	};
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
		.authorizeHttpRequests(
			(requests) -> 
			requests
			.requestMatchers(WHITE_LIST).permitAll()
			.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
			.anyRequest().authenticated()
		).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		 .csrf(csrf -> csrf.disable())
		 .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()))
		 .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
		 .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
	public KeyPair keypair() {
		try {
			var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Bean
	public RSAKey rsaKey(KeyPair keyPair) {
		return new RSAKey
				.Builder((RSAPublicKey)keyPair.getPublic())
				.privateKey(keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString())
				.build();
	}
	
	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
		var jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, context) -> jwkSelector.select(jwkSet);
	}
	
	@Bean
	public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder
				.withPublicKey(rsaKey.toRSAPublicKey())
				.build();
	}
	
	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}

}
