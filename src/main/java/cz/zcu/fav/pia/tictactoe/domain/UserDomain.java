package cz.zcu.fav.pia.tictactoe.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class UserDomain implements UserDetails {

    private UUID id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    Set<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
