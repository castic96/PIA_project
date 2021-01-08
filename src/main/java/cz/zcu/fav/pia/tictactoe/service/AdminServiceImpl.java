package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.domain.RoleEnum;
import cz.zcu.fav.pia.tictactoe.domain.UserDomain;
import cz.zcu.fav.pia.tictactoe.entity.RoleEntity;
import cz.zcu.fav.pia.tictactoe.entity.UserEntity;
import cz.zcu.fav.pia.tictactoe.repository.RoleEntityRepository;
import cz.zcu.fav.pia.tictactoe.repository.UserEntityRepository;
import cz.zcu.fav.pia.tictactoe.util.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.RequestScope;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.transaction.Transactional;
import java.util.*;

@Service("adminService")
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") //TODO: brat to z enumu
@RequestScope
public class AdminServiceImpl implements AdminService {

    private final UserEntityRepository userEntityRepository;
    private final RoleEntityRepository roleEntityRepository;

    private final UserService userService;

    private List<UserDomain> userDomainList;
    private UserDomain user;

    private String newPassword1;
    private String newPassword2;

    private final PasswordEncoder encoder;

    public List<UserDomain> getUserDomainList() {
        if (this.userDomainList == null) {
            this.userDomainList = getUsers();
        }

        return userDomainList;
    }

    private List<UserDomain> getUsers() {
        List<UserDomain> userDomainList = new LinkedList<>();

        for (UserEntity userEntity : this.userEntityRepository.findAll()) {
            if (!userEntity.isDeleted()) {

                Set<GrantedAuthority> authorities = new HashSet<>();

                for (RoleEntity currentRole : userEntity.getRoles()) {
                    authorities.add(new SimpleGrantedAuthority(Utils.toSpringRole(currentRole)));
                }

                userDomainList.add(new UserDomain(
                        userEntity.getId(),
                        userEntity.getUsername(),
                        userEntity.getPassword(),
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        authorities
                ));
            }
        }

        return Collections.unmodifiableList(userDomainList);
    }

    public void createUser() {
        this.user = UserDomain.builder().build();
    }

    public UserDomain getUser() {
        if (this.user == null) {
            this.user = new UserDomain();
        }

        return this.user;
    }

    public void deleteUser() {
        if (this.user != null) {
            deleteUser(user);
            this.user = null;
        }
    }

    @Transactional
    public void deleteUser(UserDomain userDomain) {
        List<UserEntity> adminUsers = roleEntityRepository.findRoleEntityByCode(RoleEnum.ADMIN.getCode()).getUsers();

        if (adminUsers.size() == 1 && adminUsers.get(0).getId().equals(userDomain.getId())) {
            log.error("Unable to delete last admin in application.");
            //TODO: propagovat chybu
        }
        else {
            userEntityRepository.removeUserEntityByUsername(userDomain.getUsername());
        }
    }

    public boolean isHasUsername() {
        if (this.user == null) {
            return false;
        }

        if (StringUtils.hasText(this.user.getUsername())) {
            return true;
        }

        return false;
    }

    private boolean setPasswordInternal() {
        if (!Objects.equals(newPassword1, newPassword2)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "New passwords don't match!", null));
            return false;
        }

        if (!StringUtils.hasText(newPassword1)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password cannot be empty!", null));
            return false;
        }

        String encoded = encoder.encode(newPassword1);

        //TODO: tohle pořešit, aby se mi nereplacovaly role u editace uživatele
        this.user.setPassword(encoded);
//        this.userDomain = userDomain.toBuilder()
//                .password(encoded)
//                .authorities(Set.of(new SimpleGrantedAuthority("ROLE_" + RoleEnum.USER.getCode())))
//                .build();
        return true;
    }

    public void saveUser() {
        if (this.user == null) {
            return;
        }

        if (!StringUtils.hasText(this.user.getUsername())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username must not be empty!", null));
            return;
        }

        if (this.setPasswordInternal()) {
            addUser(user);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "User successfully saved!", null));
        }
    }

    private void addUser(UserDomain userDomain) {
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(userDomain.getUsername());

        if (userEntity == null) {
            userService.addUser(
                    userDomain.getUsername(),
                    userDomain.getPassword(),
                    userDomain.getFirstName(),
                    userDomain.getLastName(),
                    RoleEnum.USER.getCode()
            );
        }
        else {
            userEntity.setPassword(userDomain.getPassword());
            userEntityRepository.save(userEntity);
        }
    }

}
