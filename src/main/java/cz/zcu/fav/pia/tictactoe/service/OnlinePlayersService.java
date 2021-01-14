package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.dto.FriendDTO;
import cz.zcu.fav.pia.tictactoe.dto.OnlinePlayerDTO;

import java.util.List;

public interface OnlinePlayersService {
    List<OnlinePlayerDTO> getOnlinePlayers(String loggedUser);

    List<String> findDisconnectedUsers(String loggedUser);

    List<FriendDTO> getFriendsWithStatus(String loggedUser);
}
