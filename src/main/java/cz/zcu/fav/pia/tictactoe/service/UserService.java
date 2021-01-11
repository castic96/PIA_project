package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.dto.UserInfoDTO;

public interface UserService {

    boolean addUser(String username, String password, String firstName, String lastName, String... roles);

    UserInfoDTO updateUser(String username, UserInfoDTO userInfoDTO);

}
