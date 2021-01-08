package cz.zcu.fav.pia.tictactoe.service;

public interface UserService {

    boolean addUser(String username, String password, String firstName, String lastName, String... roles);

}
