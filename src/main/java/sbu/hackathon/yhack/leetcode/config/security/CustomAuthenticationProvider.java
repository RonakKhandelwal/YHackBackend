package sbu.hackathon.yhack.leetcode.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sbu.hackathon.yhack.leetcode.domain.ApplicationUser;
import sbu.hackathon.yhack.leetcode.domain.User;
import sbu.hackathon.yhack.leetcode.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by Mayank Tiwari on 18/01/17.
 */
@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authenticating User...");
        Authentication auth = null;

        String userName = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();

//        if (userName.equals("admin") && password.equals("admin")) {
//            auth = generateDummyAuth();
//        } else {
        User user = userRepository.findUserByUserName(userName).orElse(null);
        if (user != null) {
            if (Objects.equals(user.getIsActive(), true)) {
                // boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
                /*if (!Objects.equals(user.getUserName(), userName) || !passwordMatches) {
                    throw new BadCredentialsException("Invalid UserName/Password");
                }*/

                log.info("User Found in DB... Checking Account Expiry Status...");
                LocalDate now = LocalDate.now();
                boolean isNotExpired = (((user.getStartDate() == null) || now.isAfter(user.getStartDate())) && (user.getEndDate() == null || now.isBefore(user.getEndDate())));
                log.info("Is Account Active?: {}", isNotExpired);
                if (isNotExpired) {
                    log.info("User Account is Active... Authenticating with LDAP...");
//                boolean authenticateLdap = (userName.equals("admin") && password.equals("admin")) || ldapClientMB.authenticateIntraLdap(userName, password);
//                log.info("Is LDAP Authentication Password?: {}", authenticateLdap);
//                if (authenticateLdap) {
                    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                    /*for (Role role : user.getRoles()) {
                        if (Objects.equals(role.getIsActive(), true)) {
                            for (RoleSecurityAccess roleSecurityAccess : role.getSecurityAccessSet()) {
                                if (Objects.equals(roleSecurityAccess.getIsActive(), true)) {
                                    grantedAuthorities.add(new SimpleGrantedAuthority(roleSecurityAccess.getSecurityLookup().getRoleName()));
                                }
                            }
                        }
                    }*/

                    ApplicationUser appUser = new ApplicationUser(user.getUserName(), password, user.getEmail(), user.getName(), true, true, true, true, grantedAuthorities);
                    auth = new UsernamePasswordAuthenticationToken(appUser, password, grantedAuthorities);
//                } else {
//                    throw new BadCredentialsException("Invalid UserName/Password");
//                }
                } else {
                    log.info("User Account Expired.");
                    throw new LockedException("User Account is Locked!");
                }
            } else {
                throw new DisabledException("User Account is Disabled.");
            }
        } else {
            log.info("User {} doesn't exists in Database.", userName);
        }
//        }
        return auth;
    }

    /*private Authentication generateDummyAuth() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        ApplicationUser appUser = new ApplicationUser("admin", "", "admin@hyperion_security.com", "Administrator", "", true, true, true, true, grantedAuthorities);
        return new UsernamePasswordAuthenticationToken(appUser, "admin", grantedAuthorities);
    }*/

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public static void main(String[] args) {

    }

}