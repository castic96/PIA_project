package cz.zcu.fav.pia.tictactoe.controller.stomp;

import cz.zcu.fav.pia.tictactoe.domain.GameDomain;
import cz.zcu.fav.pia.tictactoe.dto.ChatMessageDTO;
import cz.zcu.fav.pia.tictactoe.service.GameService;
import cz.zcu.fav.pia.tictactoe.service.LoggedUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final LoggedUserService loggedUserService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameService gameService;

    @MessageMapping("/client/chat")
    public void sendChatMessage(ChatMessageDTO chatMessage) {
        String loggedUser = loggedUserService.getUser().getUsername();
        String receiver;

        GameDomain game = gameService.loadGameByUser(loggedUser);

        if (loggedUser.equals(game.getUsername1())) {
            receiver = game.getUsername2();
        }
        else {
            receiver = game.getUsername1();
        }

        simpMessagingTemplate.convertAndSendToUser(receiver,
                "/client/chat", new ChatMessageDTO(chatMessage.getMessage(), loggedUserService.getUser().getUsername()));

    }

}
