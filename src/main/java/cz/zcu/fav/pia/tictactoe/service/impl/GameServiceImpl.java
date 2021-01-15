package cz.zcu.fav.pia.tictactoe.service.impl;

import cz.zcu.fav.pia.tictactoe.configuration.Constants;
import cz.zcu.fav.pia.tictactoe.domain.GameDomain;
import cz.zcu.fav.pia.tictactoe.repository.GameDomainRepository;
import cz.zcu.fav.pia.tictactoe.service.GameService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public void removeGame(GameDomain game) {
        gameDomainRepository.removeGame(game);
    }

    @Override
    public GameDomain loadGameByUser(String username) {
        return gameDomainRepository.loadGameByUser(username);
    }

    @Override
    public List<String> playersInGame() {
        List<String> playersInGame = new ArrayList<>();

        gameDomainRepository.getAllGames();

        for (GameDomain gameDomain : gameDomainRepository.getAllGames()) {
            if (!playersInGame.contains(gameDomain.getUsername1())) {
                playersInGame.add(gameDomain.getUsername1());
            }

            if (!playersInGame.contains(gameDomain.getUsername2())) {
                playersInGame.add(gameDomain.getUsername2());
            }
        }

        return playersInGame;
    }

    @Override
    public GameDomain giveUp(String username) {
        GameDomain game = gameDomainRepository.loadGameByUser(username);

        if (game == null) {
            return null;
        }

        if (game.getUsername1().equals(username)) {
            game.setWinner(game.getUsername2());
        }
        else {
            game.setWinner(game.getUsername1());
        }

        return game;
    }

    @Override
    public GameDomain move(String username, String positionStr) {
        GameDomain game = gameDomainRepository.loadGameByUser(username);
        List<String> positionsList = Arrays.asList(positionStr.split("-"));

        int x = Integer.parseInt(positionsList.get(0));
        int y = Integer.parseInt(positionsList.get(1));


        if (game.getUsername1().equals(username)) {
            game.getGameBoard().get(x).set(y, 1);
        }
        else {
            game.getGameBoard().get(x).set(y, 2);
        }

        if (evaluateMove(game, x, y, Constants.WINNING_SEQUENCE_COUNT, Constants.BOARD_SIZE_X, Constants.BOARD_SIZE_Y)) {
            game.setWinner(username);
        }

        return game;
    }

    private boolean evaluateMove(GameDomain game, int x, int y, int winningSequenceCount, int boardSizeX, int boardSizeY) {
        if (evaluateMoveVertical(game, x, y, winningSequenceCount, boardSizeX))
            return true;
        if (evaluateMoveHorizontal(game, x, y, winningSequenceCount, boardSizeY))
            return true;
        if (evaluateMoveMainDiagonal(game, x, y, winningSequenceCount, boardSizeX, boardSizeY))
            return true;
        if (evaluateMoveMinorDiagonal(game, x, y, winningSequenceCount, boardSizeX, boardSizeY))
            return true;

        return false;
    }

    private boolean evaluateMoveVertical(GameDomain game, int x, int y, int winningSequenceCount, int boardSizeX) {
        int count = 1;
        int mark = game.getGameBoard().get(x).get(y);

        // Smerem nahoru
        boolean searchContinue = true;

        int xTemp = x;
        int yTemp = y;

        while(searchContinue) {
            xTemp--;

            if (xTemp >= 0) {
                if (game.getGameBoard().get(xTemp).get(yTemp) == mark) {
                    count++;
                }
                else {
                    searchContinue = false;
                }
            }
            else {
                searchContinue = false;
            }
        }

        // Smerem dolu
        searchContinue = true;
        xTemp = x;
        yTemp = y;

        while(searchContinue) {
            xTemp++;

            if (xTemp < boardSizeX) {
                if (game.getGameBoard().get(xTemp).get(yTemp) == mark) {
                    count++;
                }
                else {
                    searchContinue = false;
                }
            }
            else {
                searchContinue = false;
            }
        }

        return count >= winningSequenceCount;
    }

    private boolean evaluateMoveHorizontal(GameDomain game, int x, int y, int winningSequenceCount, int boardSizeY) {
        int count = 1;
        int mark = game.getGameBoard().get(x).get(y);

        // Smerem doleva
        boolean searchContinue = true;

        int xTemp = x;
        int yTemp = y;

        while(searchContinue) {
            yTemp--;

            if (yTemp >= 0) {
                if (game.getGameBoard().get(xTemp).get(yTemp) == mark) {
                    count++;
                }
                else {
                    searchContinue = false;
                }
            }
            else {
                searchContinue = false;
            }
        }

        // Smerem vpravo
        searchContinue = true;
        xTemp = x;
        yTemp = y;

        while(searchContinue) {
            yTemp++;

            if (yTemp < boardSizeY) {
                if (game.getGameBoard().get(xTemp).get(yTemp) == mark) {
                    count++;
                }
                else {
                    searchContinue = false;
                }
            }
            else {
                searchContinue = false;
            }
        }

        return count >= winningSequenceCount;
    }

    private boolean evaluateMoveMainDiagonal(GameDomain game, int x, int y, int winningSequenceCount, int boardSizeX, int boardSizeY) {
        int count = 1;
        int mark = game.getGameBoard().get(x).get(y);

        // Smerem vlevo nahoru
        boolean searchContinue = true;

        int xTemp = x;
        int yTemp = y;

        while(searchContinue) {
            xTemp--;
            yTemp--;

            if (xTemp >= 0 && yTemp >= 0) {
                if (game.getGameBoard().get(xTemp).get(yTemp) == mark) {
                    count++;
                }
                else {
                    searchContinue = false;
                }
            }
            else {
                searchContinue = false;
            }
        }

        // Smerem vpravo dolu
        searchContinue = true;
        xTemp = x;
        yTemp = y;

        while(searchContinue) {
            xTemp++;
            yTemp++;

            if (xTemp < boardSizeX && yTemp < boardSizeY) {
                if (game.getGameBoard().get(xTemp).get(yTemp) == mark) {
                    count++;
                }
                else {
                    searchContinue = false;
                }
            }
            else {
                searchContinue = false;
            }
        }

        return count >= winningSequenceCount;
    }

    private boolean evaluateMoveMinorDiagonal(GameDomain game, int x, int y, int winningSequenceCount, int boardSizeX, int boardSizeY) {
        int count = 1;
        int mark = game.getGameBoard().get(x).get(y);

        // Smerem vpravo nahoru
        boolean searchContinue = true;

        int xTemp = x;
        int yTemp = y;

        while(searchContinue) {
            xTemp--;
            yTemp++;

            if (xTemp >= 0 && yTemp < boardSizeY) {
                if (game.getGameBoard().get(xTemp).get(yTemp) == mark) {
                    count++;
                }
                else {
                    searchContinue = false;
                }
            }
            else {
                searchContinue = false;
            }
        }

        // Smerem vlevo dolu
        searchContinue = true;
        xTemp = x;
        yTemp = y;

        while(searchContinue) {
            xTemp++;
            yTemp--;

            if (xTemp < boardSizeX && yTemp >= 0) {
                if (game.getGameBoard().get(xTemp).get(yTemp) == mark) {
                    count++;
                }
                else {
                    searchContinue = false;
                }
            }
            else {
                searchContinue = false;
            }
        }

        return count >= winningSequenceCount;
    }
}
