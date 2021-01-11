package cz.zcu.fav.pia.tictactoe.repository;

import cz.zcu.fav.pia.tictactoe.domain.GameDomain;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GameDomainRepositoryImpl implements GameDomainRepository {

    private static int id = 0;

    private List<GameDomain> games = new ArrayList<>();

    public void addGame(GameDomain game) {
        games.add(game);
    }

    public void removeGame(GameDomain game) {
        games.remove(game);
    }

    public GameDomain loadGameById(int id) {
        for (GameDomain game : games) {
            if (game.getID() == id) {
                return game;
            }
        }

        return null;
    }

    public int generateId() {
        id++;
        return id;
    }

}
