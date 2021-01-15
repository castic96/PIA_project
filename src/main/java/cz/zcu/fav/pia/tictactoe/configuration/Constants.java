package cz.zcu.fav.pia.tictactoe.configuration;

import cz.zcu.fav.pia.tictactoe.domain.RoleEnum;

/**
 * Application constants.
 */
public final class Constants {

    public static final String INIT_ADMIN_USERNAME = "admin@example.com";
    public static final String INIT_USER1_USERNAME = "user1@admin.com";
    public static final String INIT_USER2_USERNAME = "user2@admin.com";

    public static final String INIT_ADMIN_PASSWORD = "admin";
    public static final String INIT_USER1_PASSWORD = "user1";
    public static final String INIT_USER2_PASSWORD = "user2";

    public static final RoleEnum INIT_ROLE_ADMIN = RoleEnum.ADMIN;
    public static final RoleEnum INIT_ROLE_USER = RoleEnum.USER;

    public static final String LOGIN_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final int BOARD_SIZE_X = 5;
    public static final int BOARD_SIZE_Y = 5;
    public static final int WINNING_SEQUENCE_COUNT = 4;

    private Constants() {
    }
}
