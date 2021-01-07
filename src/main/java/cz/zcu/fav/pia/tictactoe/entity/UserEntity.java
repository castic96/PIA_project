package cz.zcu.fav.pia.tictactoe.entity;

import cz.zcu.fav.pia.tictactoe.configuration.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Create table user_tab.
 */
@Entity
@Table(name = "user_tab")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserEntity extends AbstractEntity {

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "password_hash", nullable = false)
    private String password;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nick_name", length = 50, nullable = false, unique = true)
    private String nickName;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "user_role_tab",
            joinColumns = @JoinColumn(name = "user_tab_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_tab_id", referencedColumnName = "id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

}
