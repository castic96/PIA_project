package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.domain.GameDomain;

import java.util.List;

public interface GameService {

    GameDomain createGame(String user1, String user2);

    void addGame(GameDomain game);

    GameDomain move(String username, String positionStr);

    void removeGame(GameDomain game);

    List<String> playersInGame();

    GameDomain giveUp(String username);

}
