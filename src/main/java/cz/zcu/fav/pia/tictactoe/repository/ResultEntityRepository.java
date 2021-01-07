package cz.zcu.fav.pia.tictactoe.repository;

import cz.zcu.fav.pia.tictactoe.entity.FriendEntity;
import cz.zcu.fav.pia.tictactoe.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResultEntityRepository extends JpaRepository<ResultEntity, UUID> {
}
