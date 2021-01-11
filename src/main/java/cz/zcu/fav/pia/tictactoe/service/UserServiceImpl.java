package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.domain.RoleEnum;
import cz.zcu.fav.pia.tictactoe.domain.UserDomain;
import cz.zcu.fav.pia.tictactoe.dto.UserInfoDTO;
import cz.zcu.fav.pia.tictactoe.entity.RoleEntity;
import cz.zcu.fav.pia.tictactoe.entity.UserEntity;
import cz.zcu.fav.pia.tictactoe.repository.RoleEntityRepository;
import cz.zcu.fav.pia.tictactoe.repository.UserEntityRepository;
import cz.zcu.fav.pia.tictactoe.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;


@Service("userService")
@RequiredArgsConstructor
@Slf4j
@DependsOn("roleService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String INIT_USERNAME = "admin@admin.com";
    private static final String INIT_PASSWORD = "admin";
    private static final RoleEnum INIT_ROLE_ADMIN = RoleEnum.ADMIN;
    private static final RoleEnum INIT_ROLE_USER = RoleEnum.USER;

    private final UserEntityRepository userEntityRepository;
    private final RoleEntityRepository roleEntityRepository;

    private final PasswordEncoder encoder;

    @PostConstruct
    private void setup() {
        if (!userEntityRepository.existsUserEntitiesByRolesEquals(roleEntityRepository.findRoleEntityByCode(INIT_ROLE_ADMIN.getCode()))) {
            log.info("No admin present, creating admin...");
            addUser(INIT_USERNAME, encoder.encode(INIT_PASSWORD), "Super", "User", INIT_ROLE_ADMIN.getCode(), INIT_ROLE_USER.getCode());
        }
    }

    @Override
    public boolean addUser(String username, String password, String firstName,
                           String lastName, String... roles) {

        if (userEntityRepository.findUserEntityByUsername(username) != null) {
            log.error("User already exists!");
            return false;
        }

        UserEntity userEntity = new UserEntity(
                username,
                password,
                firstName,
                lastName);

        for (String currentRole : roles) {
            RoleEntity currentRoleEntity = roleEntityRepository.findRoleEntityByCode(currentRole);

            if (currentRoleEntity != null) {
                userEntity.getRoles().add(currentRoleEntity);
                log.info("Role " + currentRole + " has been set to new user " + username);
            }
            else {
                log.error("Role " + currentRole + "does not exist!");
            }
        }

        try {
            userEntityRepository.save(userEntity);
        } catch (RuntimeException e) {
            log.error("Wrong format of email address.");

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong format of email address.", null));

            return false;
        }

        log.info("User successfully created.");

        return true;
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
            authorities.add(new SimpleGrantedAuthority(Utils.toSpringRole(currentRole)));
        }

        return new UserDomain(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                authorities);
    }

    @Override
    public UserInfoDTO updateUser(String username, UserInfoDTO userInfoDTO) {
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);

        userEntity.setFirstName(userInfoDTO.getFirstName());
        userEntity.setLastName(userInfoDTO.getLastName());

        userEntityRepository.save(userEntity);

        userEntity = userEntityRepository.findUserEntityByUsername(username);

        return new UserInfoDTO(userEntity.getFirstName(), userEntity.getLastName());
    }

}
