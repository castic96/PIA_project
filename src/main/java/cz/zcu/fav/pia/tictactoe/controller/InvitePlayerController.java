package cz.zcu.fav.pia.tictactoe.controller;

import cz.zcu.fav.pia.tictactoe.dto.UserDTO;
import cz.zcu.fav.pia.tictactoe.util.LoggedUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class InvitePlayerController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LoggedUserService loggedUserService;

    @MessageMapping("/invite")
    public void invitePlayer(UserDTO message) {
        log.info(message.getUsername());
        simpMessagingTemplate.convertAndSendToUser(message.getUsername(),
                "/invite", new UserDTO(loggedUserService.getUser().getUsername()));
    }

}
