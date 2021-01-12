package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.configuration.Constants;
import cz.zcu.fav.pia.tictactoe.domain.GameDomain;
import cz.zcu.fav.pia.tictactoe.repository.GameDomainRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("gameService")
@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameDomainRepository gameDomainRepository;

    @Override
    public GameDomain createGame(String user1, String user2) {

        return new GameDomain(
                gameDomainRepository.generateId(),
                user1,
                user2,
                null,
                user1,
                initGameBoard()
        );

    }

    private List<List<Integer>> initGameBoard() {
        List<List<Integer>> board = new ArrayList<>(Constants.BOARD_SIZE_X);

        for (int i = 0; i < Constants.BOARD_SIZE_X; i++) {
            List<Integer> newList = new ArrayList<>(Constants.BOARD_SIZE_Y);

            for (int j = 0; j < Constants.BOARD_SIZE_Y; j++) {
                newList.add(0);
            }

            board.add(newList);
        }

        return board;
    }

    @Override
    public void addGame(GameDomain game) {
        gameDomainRepository.addGame(game);
    }

}
