package cz.zcu.fav.pia.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDomain {

    private String firstName;
    private String lastName;

}
