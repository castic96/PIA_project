package cz.zcu.fav.pia.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class ResultDomain {

    private String opponentUsername;

    private boolean winner;

    private LocalDateTime dateTime;

    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy HH:mm:ss");
        return this.dateTime.format(formatter);
    }

}
