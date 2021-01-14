package cz.zcu.fav.pia.tictactoe.component;

import cz.zcu.fav.pia.tictactoe.domain.GameDomain;
import cz.zcu.fav.pia.tictactoe.service.GameService;
import cz.zcu.fav.pia.tictactoe.service.OnlinePlayersService;
import cz.zcu.fav.pia.tictactoe.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OnlinePlayersComponent {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameService gameService;
    private final OnlinePlayersService onlinePlayersService;
    private final ResultService resultService;

    @Scheduled(fixedRate = 1000)
    public void onlinePlayers() {
        simpMessagingTemplate.convertAndSend("/client/online-players", onlinePlayersService.getOnlinePlayers());

        List<String> disconnectedPlayers = onlinePlayersService.findDisconnectedUsers();

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
