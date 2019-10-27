package sbu.hackathon.yhack.leetcode.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Mayank Tiwari on 18/01/17.
 */
@Slf4j
@Getter
public class ApplicationUser extends org.springframework.security.core.userdetails.User {

    private final String email;
    private final Name name;

    public ApplicationUser(String username, String password, String email, Name name,
                           boolean enabled,
                           boolean accountNonExpired, boolean credentialsNonExpired,
                           boolean accountNonLocked,
                           Collection<GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.email = email;
        this.name = name;

        log.info("Assigned Authorities for User: {}", username);
        for (GrantedAuthority authority : authorities) {
            log.info("\t->{}", authority.getAuthority());
        }
    }
}