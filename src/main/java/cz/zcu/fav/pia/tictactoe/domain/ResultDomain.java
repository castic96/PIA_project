package cz.zcu.fav.pia.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResultDomain {

    private String opponentUsername;

    private boolean winner;

    private LocalDateTime dateTime;

}
