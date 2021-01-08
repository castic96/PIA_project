package cz.zcu.fav.pia.tictactoe.repository;

import cz.zcu.fav.pia.tictactoe.entity.RoleEntity;
import cz.zcu.fav.pia.tictactoe.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findUserEntityByUsername(String username);

    boolean existsUserEntitiesByRolesEquals(RoleEntity role);

    void removeUserEntityByUsername(String username);

}
