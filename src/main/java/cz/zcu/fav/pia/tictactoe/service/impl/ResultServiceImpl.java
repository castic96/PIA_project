package cz.zcu.fav.pia.tictactoe.service.impl;

import cz.zcu.fav.pia.tictactoe.entity.ResultEntity;
import cz.zcu.fav.pia.tictactoe.entity.UserEntity;
import cz.zcu.fav.pia.tictactoe.repository.ResultEntityRepository;
import cz.zcu.fav.pia.tictactoe.repository.UserEntityRepository;
import cz.zcu.fav.pia.tictactoe.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("resultService")
@RequiredArgsConstructor
@Slf4j
public class ResultServiceImpl implements ResultService {

    private final UserEntityRepository userEntityRepository;
    private final ResultEntityRepository resultEntityRepository;

    public void addResult(String username1, String username2, String winner) {
        UserEntity user1 = userEntityRepository.findUserEntityByUsername(username1);
        UserEntity user2 = userEntityRepository.findUserEntityByUsername(username2);

        ResultEntity resultEntity;

        if (username1.equals(winner)) {
            resultEntity = new ResultEntity(
                    user1,
                    user2,
                    user1
            );
        }
        else {
            resultEntity = new ResultEntity(
                    user1,
                    user2,
                    user2
            );
        }

        resultEntityRepository.save(resultEntity);

    }
}
