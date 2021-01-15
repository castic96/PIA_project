package cz.zcu.fav.pia.tictactoe.controller.stomp;

import cz.zcu.fav.pia.tictactoe.domain.UserDomain;
import cz.zcu.fav.pia.tictactoe.dto.AcceptationDTO;
import cz.zcu.fav.pia.tictactoe.dto.FriendDTO;
import cz.zcu.fav.pia.tictactoe.dto.UserDTO;
import cz.zcu.fav.pia.tictactoe.service.FriendService;
import cz.zcu.fav.pia.tictactoe.service.LoggedUserService;
import cz.zcu.fav.pia.tictactoe.service.OnlinePlayersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FriendController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LoggedUserService loggedUserService;
    private final FriendService friendService;
    private final UserDetailsService userDetailsService;
    private final OnlinePlayersService onlinePlayersService;

    @MessageMapping("/friend/add")
    public void addFriend(UserDTO message) {

        simpMessagingTemplate.convertAndSendToUser(message.getUsername(),
                "/friend/ask", new UserDTO(loggedUserService.getUser().getUsername()));
    }

    @MessageMapping("/friend/remove")
    public void removeFriend(UserDTO message) {

        friendService.removeFriend((UserDomain) userDetailsService.loadUserByUsername(message.getUsername()));

        simpMessagingTemplate.convertAndSendToUser(message.getUsername(),
                "/friend/removed", new UserDTO(loggedUserService.getUser().getUsername()));
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

    @MessageMapping("/client/friends")
    public void getFriends() {
        String loggedUser = loggedUserService.getUser().getUsername();
        List<FriendDTO> friendsWithStatus = onlinePlayersService.getFriendsWithStatus(loggedUser);

        simpMessagingTemplate.convertAndSendToUser(loggedUserService.getUser().getUsername(), "/client/friends", friendsWithStatus);
    }

}
