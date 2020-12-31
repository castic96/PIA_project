package cz.zcu.fav.pia.tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class TicTacToeApp {

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeApp.class, args);
    }

}
