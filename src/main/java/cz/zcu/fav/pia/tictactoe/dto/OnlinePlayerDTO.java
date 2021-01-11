package cz.zcu.fav.pia.tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnlinePlayerDTO {

    private String username;
    private boolean inGame;

}
