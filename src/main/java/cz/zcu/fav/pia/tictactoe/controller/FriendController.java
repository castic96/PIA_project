package cz.zcu.fav.pia.tictactoe.controller;

import cz.zcu.fav.pia.tictactoe.domain.UserDomain;
import cz.zcu.fav.pia.tictactoe.dto.AcceptationDTO;
import cz.zcu.fav.pia.tictactoe.dto.UserDTO;
import cz.zcu.fav.pia.tictactoe.service.FriendService;
import cz.zcu.fav.pia.tictactoe.service.LoggedUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FriendController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LoggedUserService loggedUserService;
    private final FriendService friendService;
    private final UserDetailsService userDetailsService;

    @MessageMapping("/friend/add")
    public void addFriend(UserDTO message) {

        simpMessagingTemplate.convertAndSendToUser(message.getUsername(),
                "/friend/ask", new UserDTO(loggedUserService.getUser().getUsername()));
    }

    @MessageMapping("/friend/acceptation")
    public void acceptFriend(AcceptationDTO message) {
        boolean accepted = message.isAccepted();

        String destination = accepted ? "/friend/accept" : "/friend/decline";

        if (accepted) {
            friendService.addFriend((UserDomain) userDetailsService.loadUserByUsername(message.getUsername()));
        }

        simpMessagingTemplate.convertAndSendToUser(message.getUsername(),
                destination, new UserDTO(loggedUserService.getUser().getUsername()));
    }

}
