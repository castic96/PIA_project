package cz.zcu.fav.pia.tictactoe.repository.impl;

import cz.zcu.fav.pia.tictactoe.domain.GameDomain;
import cz.zcu.fav.pia.tictactoe.repository.GameDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;

@Repository
@ApplicationScope
public class GameDomainRepositoryImpl implements GameDomainRepository {

    private static int id = 0;

    private List<GameDomain> games = new ArrayList<>();

    @Override
    public void addGame(GameDomain game) {
        games.add(game);
    }

    @Override
    public void removeGame(GameDomain game) {
        games.remove(game);
    }

    @Override
    public List<GameDomain> getAllGames() {
        return this.games;
    }

    @Override
    public GameDomain loadGameById(int id) {
        for (GameDomain game : games) {
            if (game.getID() == id) {
                return game;
            }
        }

        return null;
    }

    @Override
    public GameDomain loadGameByUser(String username) {
        for (GameDomain game : games) {
            if (game.getUsername1().equals(username) || game.getUsername2().equals(username)) {
                return game;
            }
        }

        return null;
    }

    @Override
    public int generateId() {
        id++;
        return id;
    }

}
