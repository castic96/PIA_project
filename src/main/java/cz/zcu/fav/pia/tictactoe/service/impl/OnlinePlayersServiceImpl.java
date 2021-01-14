package cz.zcu.fav.pia.tictactoe.service.impl;

import cz.zcu.fav.pia.tictactoe.dto.OnlinePlayerDTO;
import cz.zcu.fav.pia.tictactoe.service.GameService;
import cz.zcu.fav.pia.tictactoe.service.LoggedUserService;
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

    public List<OnlinePlayerDTO> getOnlinePlayers() {
        List<OnlinePlayerDTO> onlinePlayers = new ArrayList<>();
        List<String> onlinePlayersStr;
        boolean inGame;

        onlinePlayersStr = simpUserRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toList());

        LoggedUserService loggedUserService = new LoggedUserServiceImpl();

        List<String> playersInGame = gameService.playersInGame();

        for(String onlinePlayerStr : onlinePlayersStr) {
            if (loggedUserService.getUser() != null) {
                String loggedUser = loggedUserService.getUser().getUsername();

                if ((onlinePlayerStr.equals(loggedUser)))
                {
                    continue;
                }

            }

            inGame = playersInGame.contains(onlinePlayerStr);

            onlinePlayers.add(new OnlinePlayerDTO(onlinePlayerStr, inGame));
        }

        return onlinePlayers;
    }

    public List<String> findDisconnectedUsers() {
        List<String> disconnectedUsers = new ArrayList<>();
        boolean online;

        List<String> playersInGame = gameService.playersInGame();
        List<OnlinePlayerDTO> onlineUsers = getOnlinePlayers();

        for (String player : playersInGame) {
            online = false;

            for (OnlinePlayerDTO onlineUser : onlineUsers) {

                if (player.equals(onlineUser.getUsername())) {
                    online = true;
                    break;
                }

            }

            if (!online) {
                disconnectedUsers.add(player);
            }

        }

        return disconnectedUsers;
    }

}
