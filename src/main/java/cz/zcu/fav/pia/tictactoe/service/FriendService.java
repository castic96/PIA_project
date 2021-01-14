package cz.zcu.fav.pia.tictactoe.service;

import cz.zcu.fav.pia.tictactoe.domain.UserDomain;

public interface FriendService {

    void addFriend(UserDomain loggedUser, UserDomain friend);

    void addFriend(UserDomain friend);

    void removeFriend(UserDomain friend);

    boolean areFriends(String username1, String username2);
}
