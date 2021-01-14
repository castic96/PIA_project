package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.dto.OnlinePlayerDTO;

import java.util.List;

public interface OnlinePlayersService {
    List<OnlinePlayerDTO> getOnlinePlayers();

    List<String> findDisconnectedUsers();
}
