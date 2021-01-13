package cz.zcu.fav.pia.tictactoe.util;

import cz.zcu.fav.pia.tictactoe.dto.OnlinePlayerDTO;
import cz.zcu.fav.pia.tictactoe.service.GameService;
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
        boolean inGame = false;

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

}
