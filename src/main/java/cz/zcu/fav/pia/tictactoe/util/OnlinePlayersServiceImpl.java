package cz.zcu.fav.pia.tictactoe.util;

import cz.zcu.fav.pia.tictactoe.dto.OnlinePlayer;
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

    public List<OnlinePlayer> getOnlinePlayers() {
        List<OnlinePlayer> onlinePlayers = new ArrayList<>();
        List<String> onlinePlayersStr;

        onlinePlayersStr = simpUserRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toList());

        LoggedUserService loggedUserService = new LoggedUserServiceImpl();

        for(String onlinePlayerStr : onlinePlayersStr) {
            if (loggedUserService.getUser() != null) {
                String loggedUser = loggedUserService.getUser().getUsername();

                if ((onlinePlayerStr.equals(loggedUser)))
                {
                    continue;
                }

            }

            onlinePlayers.add(new OnlinePlayer(onlinePlayerStr, false));
        }

        return onlinePlayers;
    }

}
