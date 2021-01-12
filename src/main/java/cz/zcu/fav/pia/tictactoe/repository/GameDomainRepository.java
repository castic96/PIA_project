package cz.zcu.fav.pia.tictactoe.repository;

import cz.zcu.fav.pia.tictactoe.domain.GameDomain;

public interface GameDomainRepository {

    GameDomain loadGameById(int id);

    void removeGame(GameDomain game);

    void addGame(GameDomain game);

    int generateId();

    GameDomain loadGameByUser(String username);

}
