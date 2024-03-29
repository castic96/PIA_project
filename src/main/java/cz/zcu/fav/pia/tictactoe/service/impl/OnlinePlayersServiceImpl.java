package cz.zcu.fav.pia.tictactoe.service.impl;

import cz.zcu.fav.pia.tictactoe.domain.UserDomain;
import cz.zcu.fav.pia.tictactoe.dto.FriendDTO;
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

    @Override
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
            inFriendList = friendService.areFriends(loggedUser, onlinePlayerStr);

            onlinePlayers.add(new OnlinePlayerDTO(onlinePlayerStr, inGame, inFriendList));
        }

        return onlinePlayers;
    }

    @Override
    public List<FriendDTO> getFriendsWithStatus(String loggedUser) {
        List<FriendDTO> friendsWithStatus = new ArrayList<>();
        int status = 0;

        List<OnlinePlayerDTO> onlinePlayers = getOnlinePlayers(loggedUser);

        for (UserDomain friend : friendService.getFriends(loggedUser)) {
            status = 0;

            for (OnlinePlayerDTO onlinePlayer : onlinePlayers) {
                if (onlinePlayer.getUsername().equals(friend.getUsername())) {

                    if (onlinePlayer.isInGame()) {
                        status = 2;
                    }
                    else {
                        status = 1;
                    }

                    break;

                }
            }

            friendsWithStatus.add(new FriendDTO(friend.getUsername(), status));

        }

        return friendsWithStatus;
    }


    @Override
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
