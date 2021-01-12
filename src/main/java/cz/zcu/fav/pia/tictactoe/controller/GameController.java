package cz.zcu.fav.pia.tictactoe.controller;

import cz.zcu.fav.pia.tictactoe.domain.GameDomain;
import cz.zcu.fav.pia.tictactoe.dto.GameAcceptationDTO;
import cz.zcu.fav.pia.tictactoe.dto.MoveDTO;
import cz.zcu.fav.pia.tictactoe.dto.UserDTO;
import cz.zcu.fav.pia.tictactoe.service.GameService;
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

    @MessageMapping("/game/invite")
    public void invitePlayer(UserDTO message) {
        log.info(message.getUsername());
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

        GameDomain updatedGame = gameService.move(loggedUserService.getUser().getUsername(), message.getPosition());

        updateGameBoard(updatedGame);
    }

    private void updateGameBoard(GameDomain game) {
        if (game.getWinner() != null) {
            //TODO: zapsat statistiky
            simpMessagingTemplate.convertAndSendToUser(game.getUsername1(),
                    "/game/state", game);

            simpMessagingTemplate.convertAndSendToUser(game.getUsername2(),
                    "/game/state", game);

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
            //TODO: zrušit hru v listu her
        }
        else {
            simpMessagingTemplate.convertAndSendToUser(game.getUsername1(),
                    "/game/state", game);

            simpMessagingTemplate.convertAndSendToUser(game.getUsername2(),
                    "/game/state", game);
        }






    }



}