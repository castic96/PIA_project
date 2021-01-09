package cz.zcu.fav.pia.tictactoe.util;

import cz.zcu.fav.pia.tictactoe.entity.RoleEntity;

public final class Utils {

    public static String toSpringRole(RoleEntity roleEntity) {
        return "ROLE_" + roleEntity.getCode();
    }

    public static String toSpringRole(String roleString) {
        return "ROLE_" + roleString;
    }

}
