package cz.zcu.fav.pia.tictactoe.controller;

import cz.zcu.fav.pia.tictactoe.domain.GameDomain;
import cz.zcu.fav.pia.tictactoe.dto.GameAcceptationDTO;
import cz.zcu.fav.pia.tictactoe.dto.MoveDTO;
import cz.zcu.fav.pia.tictactoe.dto.UserDTO;
import cz.zcu.fav.pia.tictactoe.service.GameService;
import cz.zcu.fav.pia.tictactoe.service.ResultService;
import cz.zcu.fav.pia.tictactoe.util.LoggedUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class GameController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LoggedUserService loggedUserService;
    private final GameService gameService;
    private final ResultService resultService;

    @MessageMapping("/game/invite")
    public void invitePlayer(UserDTO message) {
        log.info(message.getUsername());
        //TODO: kontrolovat, zda hrac uz nehraje
        simpMessagingTemplate.convertAndSendToUser(message.getUsername(),
                "/game/invite", new UserDTO(loggedUserService.getUser().getUsername()));
    }

    @MessageMapping("/game/acceptation")
    public void gameAcceptation(GameAcceptationDTO message) {
        String destination = message.isAccepted() ? "/game/accept" : "/game/decline";

        simpMessagingTemplate.convertAndSendToUser(message.getUsername(),
                destination, new UserDTO(loggedUserService.getUser().getUsername()));
    }

    @MessageMapping("/game/create")
    public void createGame(UserDTO message) {
        log.info("create request...");
        log.info(message.getUsername());

        GameDomain newGame = gameService.createGame(loggedUserService.getUser().getUsername(), message.getUsername());
        gameService.addGame(newGame);

        updateGameBoard(newGame);
    }

    @MessageMapping("/game/move")
    public void moveInGame(MoveDTO message) {
        //TODO: overit ze hraje spravny hrac.

        GameDomain updatedGame = gameService.move(loggedUserService.getUser().getUsername(), message.getPosition());

        //TODO: poslat incorrect move, ale nepřepnu uživatele co je na řadě, tak se hraje dál

        if (loggedUserService.getUser().getUsername().equals(updatedGame.getUsername1())) {
            updatedGame.setCurrentUserName(updatedGame.getUsername2());
        }
        else {
            updatedGame.setCurrentUserName(updatedGame.getUsername1());
        }

        updateGameBoard(updatedGame);
    }

    @MessageMapping("/game/give-up")
    public void giveUpGame() {

        GameDomain game = gameService.giveUp(loggedUserService.getUser().getUsername());

        if (game.getWinner().equals(game.getUsername1())) {
            simpMessagingTemplate.convertAndSendToUser(game.getUsername1(),
                    "/game/win-give-up", game);
            simpMessagingTemplate.convertAndSendToUser(game.getUsername2(),
                    "/game/lose-give-up", game);
        }
        else {
            simpMessagingTemplate.convertAndSendToUser(game.getUsername2(),
                    "/game/win-give-up", game);
            simpMessagingTemplate.convertAndSendToUser(game.getUsername1(),
                    "/game/lose-give-up", game);
        }

        resultService.addResult(game.getUsername1(), game.getUsername2(), game.getWinner());
        gameService.removeGame(game);

    }

    private void updateGameBoard(GameDomain game) {
        if (game.getWinner() != null) {

            if (game.getWinner().equals(game.getUsername1())) {
                simpMessagingTemplate.convertAndSendToUser(game.getUsername1(),
                        "/game/win", game);
                simpMessagingTemplate.convertAndSendToUser(game.getUsername2(),
                        "/game/lose", game);
            }
            else {
                simpMessagingTemplate.convertAndSendToUser(game.getUsername2(),
                        "/game/win", game);
                simpMessagingTemplate.convertAndSendToUser(game.getUsername1(),
                        "/game/lose", game);
            }

            resultService.addResult(game.getUsername1(), game.getUsername2(), game.getWinner());
            gameService.removeGame(game);

        }
        else {

            if (game.getCurrentUserName().equals(game.getUsername1())) {
                simpMessagingTemplate.convertAndSendToUser(game.getUsername1(),
                        "/game/state/my-turn", game);

                simpMessagingTemplate.convertAndSendToUser(game.getUsername2(),
                        "/game/state/opp-turn", game);
            }
            else {
                simpMessagingTemplate.convertAndSendToUser(game.getUsername2(),
                        "/game/state/my-turn", game);

                simpMessagingTemplate.convertAndSendToUser(game.getUsername1(),
                        "/game/state/opp-turn", game);
            }
        }
    }
}
