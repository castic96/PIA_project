package cz.zcu.fav.pia.tictactoe.service.impl;

import cz.zcu.fav.pia.tictactoe.dto.OnlinePlayerDTO;
import cz.zcu.fav.pia.tictactoe.service.FriendService;
import cz.zcu.fav.pia.tictactoe.service.GameService;
import cz.zcu.fav.pia.tictactoe.service.OnlinePlayersService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("onlinePlayers")
@RequiredArgsConstructor
public class OnlinePlayersServiceImpl implements OnlinePlayersService {

    private final SimpUserRegistry simpUserRegistry;
    private final GameService gameService;
    private final FriendService friendService;

    public List<OnlinePlayerDTO> getOnlinePlayers(String loggedUser) {
        List<OnlinePlayerDTO> onlinePlayers = new ArrayList<>();
        List<String> onlinePlayersStr;
        boolean inGame;
        boolean inFriendList;

        onlinePlayersStr = simpUserRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toList());

        List<String> playersInGame = gameService.playersInGame();

        for(String onlinePlayerStr : onlinePlayersStr) {

            if (loggedUser != null) {
                if (onlinePlayerStr.equals(loggedUser))
                    continue;
            }

            inGame = playersInGame.contains(onlinePlayerStr);
            //inFriendList = loggedUserService.getUser() != null;

            onlinePlayers.add(new OnlinePlayerDTO(onlinePlayerStr, inGame, true));
        }

        return onlinePlayers;
    }

    public List<String> findDisconnectedUsers(String loggedUser) {
        List<String> disconnectedUsers = new ArrayList<>();
        boolean online;

        List<String> playersInGame = gameService.playersInGame();
        List<OnlinePlayerDTO> onlineUsers = getOnlinePlayers(loggedUser);

        for (String player : playersInGame) {
            online = false;

            for (OnlinePlayerDTO onlineUser : onlineUsers) {

                if (player.equals(onlineUser.getUsername())) {
                    online = true;
                    break;
                }

            }

            if (player.equals(loggedUser)) {
                online = true;
            }

            if (!online) {
                disconnectedUsers.add(player);
            }

        }

        return disconnectedUsers;
    }

}
