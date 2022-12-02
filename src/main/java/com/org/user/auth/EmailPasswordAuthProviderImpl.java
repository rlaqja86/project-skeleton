package com.org.user.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmailPasswordAuthProviderImpl implements AuthenticationProvider {
    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = retrieveUser(authentication.getName());
        CustomEmailPasswordToken customEmailPasswordToken = new CustomEmailPasswordToken(user.getUsername(),authentication.getCredentials(), null);
        customEmailPasswordToken.setDetails(authentication.getDetails());
        additionalCredentialCheck(authentication, user);

        return customEmailPasswordToken;
    }

    private void additionalCredentialCheck(Authentication authentication, UserDetails user) {
        if (authentication.getCredentials() == null) {
            log.warn("Authentication failed: no credentials provided");

            throw new BadCredentialsException(
                    "EmailPasswordAuthProviderImpl : bad Credentials");
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(presentedPassword, user.getPassword())) {
            log.warn("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(
                    "EmailPasswordAuthProviderImpl : Password not matched");
        }
    }

    protected final UserDetails retrieveUser(String userEmail) {
        return userDetailService.loadUserByUsername(userEmail);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomEmailPasswordToken.class);
    }
}
