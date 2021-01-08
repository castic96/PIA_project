package cz.zcu.fav.pia.tictactoe.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Create table friend_tab.
 */
@Entity
@Table(name = "friend_tab")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FriendEntity extends AbstractEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_1", referencedColumnName = "id")
    private UserEntity user1;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_2", referencedColumnName = "id")
    private UserEntity user2;

}