package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.domain.UserDomain;
import cz.zcu.fav.pia.tictactoe.entity.RoleEntity;
import cz.zcu.fav.pia.tictactoe.entity.UserEntity;
import cz.zcu.fav.pia.tictactoe.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;


@Service("userService")
@RequiredArgsConstructor
@Slf4j
@DependsOn("roleService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserEntityRepository userEntityRepository;
//
//    @PostConstruct
//    private void setup() {
//        addUser("user", PASSWORD, USER);
//        addUser("admin", PASSWORD, ADMIN);
//        addUser("god", PASSWORD, ADMIN, USER);
//    }

    private String toSpringRole(RoleEntity roleEntity) {
        return "ROLE_" + roleEntity.getCode();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);

        if (userEntity == null || userEntity.isDeleted()) {
            log.info("User " + username + " not found or has been deleted.");
            throw new UsernameNotFoundException("Invalid user!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (RoleEntity currentRole : userEntity.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(toSpringRole(currentRole)));
        }

        return new UserDomain(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                authorities);
    }

}
