package cz.zcu.fav.pia.tictactoe.configuration;

/**
 * Application constants.
 */
public final class Constants {

    // Email address regex
    public static final String LOGIN_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final int BOARD_SIZE_X = 5;
    public static final int BOARD_SIZE_Y = 5;
    public static final int WINNING_SEQUENCE_COUNT = 3;

    /*
    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String STANDARD_GAME = "STANDARD_GAME";

    public static final String RUNNING_GAME = "RUNNING";
    public static final String ENDED_GAME = "ENDED";
    public static final String WON_GAME = "WON";
    public static final String DRAFT_GAME = "DRAFT";

    public static final String ADD_FRIEND = "ADD_FRIEND";
    public static final String GAME_CHALLENGE = "GAME_CHALLENGE";
    public static final String GAME_ACCEPTED = "GAME_ACCEPTED";
    public static final String GAME_REJECTED = "GAME_REJECTED";
    public static final String ADD_MOVE = "ADD_MOVE";
    public static final String GIVE_UP = "GIVE_UP";
    public static final String CHALLENGE_CANCELLED = "CHALLENGE_CANCELLED";

     */

    private Constants() {
    }
}
