package cz.zcu.fav.pia.tictactoe.repository;

import cz.zcu.fav.pia.tictactoe.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResultEntityRepository extends JpaRepository<ResultEntity, UUID> {

    List<ResultEntity> findAllByUser1Username(String user1Username);

    List<ResultEntity> findAllByUser2Username(String user2Username);

}
