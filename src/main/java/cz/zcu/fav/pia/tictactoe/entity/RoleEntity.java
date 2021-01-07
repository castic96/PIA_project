package cz.zcu.fav.pia.tictactoe.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Create table role_tab.
 */
@Entity
@Table(name = "role_tab")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RoleEntity extends AbstractEntity {

    @NotNull
    @Size(min = 1, max = 20)
    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String name;

    public RoleEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
