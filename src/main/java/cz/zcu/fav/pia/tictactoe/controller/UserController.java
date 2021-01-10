package cz.zcu.fav.pia.tictactoe.controller;

import cz.zcu.fav.pia.tictactoe.domain.UserDomain;
import cz.zcu.fav.pia.tictactoe.service.FriendService;
import cz.zcu.fav.pia.tictactoe.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final FriendService friendService;
    private final UserServiceImpl userService;

    @PutMapping("/save")
    public String saveUserInfo(@RequestBody String value) {
        log.info("value: " + value);
        friendService.removeFriend((UserDomain) userService.loadUserByUsername("user1@example.com"));
        return "Styl";
    }

}
