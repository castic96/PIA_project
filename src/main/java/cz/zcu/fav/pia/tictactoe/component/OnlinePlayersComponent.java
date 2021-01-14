package cz.zcu.fav.pia.tictactoe.component;

import cz.zcu.fav.pia.tictactoe.service.OnlinePlayersService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnlinePlayersComponent {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final OnlinePlayersService onlinePlayersService;

    @Scheduled(fixedRate = 1000)
    public void onlinePlayers() {
        simpMessagingTemplate.convertAndSend("/client/online-players", onlinePlayersService.getOnlinePlayers());
    }

}
