package cz.zcu.fav.pia.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameDomain {

    private int ID;

    private String username1;

    private String username2;

    private String winner;

    private String currentUserName;

    List<List<Integer>> gameBoard;

}
