package cz.zcu.fav.pia.tictactoe.util;

import cz.zcu.fav.pia.tictactoe.dto.OnlinePlayerDTO;

import java.util.List;

public interface OnlinePlayersService {
    List<OnlinePlayerDTO> getOnlinePlayers();
}
