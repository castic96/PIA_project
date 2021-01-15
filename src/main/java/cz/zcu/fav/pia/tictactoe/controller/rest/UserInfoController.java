package cz.zcu.fav.pia.tictactoe.controller.rest;

import cz.zcu.fav.pia.tictactoe.controller.generated.UserApi;
import cz.zcu.fav.pia.tictactoe.domain.UserInfoDomain;
import cz.zcu.fav.pia.tictactoe.dto.generated.UserInfoDTO;
import cz.zcu.fav.pia.tictactoe.service.LoggedUserService;
import cz.zcu.fav.pia.tictactoe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserInfoController implements UserApi {

    private final LoggedUserService loggedUserService;
    private final UserService userService;

    @Override
    public ResponseEntity<UserInfoDTO> updateUserInfo(UserInfoDTO userInfoDTO) {
        UserInfoDomain newUserInfoDomain = userService.updateUser(
                loggedUserService.getUser().getUsername(),
                new UserInfoDomain(userInfoDTO.getFirstName(), userInfoDTO.getLastName()));

        loggedUserService.getUser().setFirstName(newUserInfoDomain.getFirstName());
        loggedUserService.getUser().setLastName(newUserInfoDomain.getLastName());

        UserInfoDTO userInfoDTO1 = new UserInfoDTO();
        userInfoDTO1.setFirstName(newUserInfoDomain.getFirstName());
        userInfoDTO1.setLastName(newUserInfoDomain.getLastName());

        return new ResponseEntity<>(userInfoDTO1, HttpStatus.OK);
    }

}
