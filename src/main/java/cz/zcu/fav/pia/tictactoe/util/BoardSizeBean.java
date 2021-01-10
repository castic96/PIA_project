package cz.zcu.fav.pia.tictactoe.util;

import cz.zcu.fav.pia.tictactoe.configuration.Constants;
import lombok.Getter;

import javax.annotation.ManagedBean;
import java.util.ArrayList;
import java.util.List;

@ManagedBean("boardSize")
@Getter
public class BoardSizeBean {

    private List<String> boardSizeX;
    private List<String> boardSizeY;

    public BoardSizeBean() {
        boardSizeX = new ArrayList<>();
        boardSizeY = new ArrayList<>();

        initBoardSize(boardSizeX, Constants.BOARD_SIZE_X);
        initBoardSize(boardSizeY, Constants.BOARD_SIZE_Y);
    }

    private void initBoardSize(List<String> boardSizeList, int boardSize) {
        for (int i = 0; i < boardSize; i++) {
            boardSizeList.add(Integer.toString(i));
        }
    }

}
