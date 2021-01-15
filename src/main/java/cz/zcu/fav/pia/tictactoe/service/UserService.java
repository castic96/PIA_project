package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.domain.UserInfoDomain;

public interface UserService {

    boolean addUser(String username, String password, String firstName, String lastName, String... roles);

    UserInfoDomain updateUser(String username, UserInfoDomain userInfoDomain);

}
