package cz.zcu.fav.pia.tictactoe.controller;

import cz.zcu.fav.pia.tictactoe.dto.UserInfoDTO;
import cz.zcu.fav.pia.tictactoe.service.UserService;
import cz.zcu.fav.pia.tictactoe.util.LoggedUserService;
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
public class UserInfoController {

    private final LoggedUserService loggedUserService;
    private final UserService userService;

    @PutMapping("/save")
    public UserInfoDTO saveUserInfo(@RequestBody UserInfoDTO userInfoDTO) {
        return userService.updateUser(loggedUserService.getUser().getUsername(), userInfoDTO);
    }

}
