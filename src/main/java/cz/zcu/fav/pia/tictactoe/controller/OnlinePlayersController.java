package cz.zcu.fav.pia.tictactoe.controller;

import cz.zcu.fav.pia.tictactoe.domain.GameDomain;
import cz.zcu.fav.pia.tictactoe.dto.OnlinePlayerDTO;
import cz.zcu.fav.pia.tictactoe.service.GameService;
import cz.zcu.fav.pia.tictactoe.service.LoggedUserService;
import cz.zcu.fav.pia.tictactoe.service.OnlinePlayersService;
import cz.zcu.fav.pia.tictactoe.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OnlinePlayersController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameService gameService;
    private final OnlinePlayersService onlinePlayersService;
    private final ResultService resultService;
    private final LoggedUserService loggedUserService;

    @MessageMapping("/client/online-players")
    public void onlinePlayers() {
        String loggedUser = loggedUserService.getUser().getUsername();
        List<OnlinePlayerDTO> onlinePlayers = onlinePlayersService.getOnlinePlayers(loggedUser);

        simpMessagingTemplate.convertAndSendToUser(loggedUserService.getUser().getUsername(), "/client/online-players", onlinePlayers);

        List<String> disconnectedPlayers = onlinePlayersService.findDisconnectedUsers(loggedUser);

        if (!disconnectedPlayers.isEmpty()) {
            endDisconnectedGames(disconnectedPlayers);
        }

    }

    private void endDisconnectedGames(List<String> disconnectedPlayers) {
        GameDomain game;

        for (String disconnectedPlayer : disconnectedPlayers) {
            game = gameService.giveUp(disconnectedPlayer);

            if (game == null) {
                continue;
            }

            if (game.getWinner().equals(game.getUsername1())) {
                simpMessagingTemplate.convertAndSendToUser(game.getUsername1(),
                        "/game/win-disconn", game);
            }
            else {
                simpMessagingTemplate.convertAndSendToUser(game.getUsername2(),
                        "/game/win-disconn", game);
            }

            resultService.addResult(game.getUsername1(), game.getUsername2(), game.getWinner());
            gameService.removeGame(game);

        }

    }

}
