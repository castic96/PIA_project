package cz.zcu.fav.pia.tictactoe.util;

import cz.zcu.fav.pia.tictactoe.dto.OnlinePlayer;

import java.util.List;

public interface OnlinePlayersService {
    List<OnlinePlayer> getOnlinePlayers();
}
