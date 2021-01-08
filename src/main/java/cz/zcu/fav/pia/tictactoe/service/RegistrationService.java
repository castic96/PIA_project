package cz.zcu.fav.pia.tictactoe.service;

public interface RegistrationService {

    void registerUser(String email, String newPassword1, String newPassword2, String firstName, String lastName);

}
