package cz.zcu.fav.pia.tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameAcceptationDTO {

    String username;
    boolean accepted;

}
