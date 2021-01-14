package cz.zcu.fav.pia.tictactoe.service.impl;

import cz.zcu.fav.pia.tictactoe.domain.UserDomain;
import cz.zcu.fav.pia.tictactoe.entity.FriendEntity;
import cz.zcu.fav.pia.tictactoe.entity.RoleEntity;
import cz.zcu.fav.pia.tictactoe.entity.UserEntity;
import cz.zcu.fav.pia.tictactoe.repository.FriendEntityRepository;
import cz.zcu.fav.pia.tictactoe.repository.UserEntityRepository;
import cz.zcu.fav.pia.tictactoe.service.FriendService;
import cz.zcu.fav.pia.tictactoe.service.LoggedUserService;
import cz.zcu.fav.pia.tictactoe.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service("friendService")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("isAuthenticated()")
public class FriendServiceImpl implements FriendService {

    private final UserEntityRepository userEntityRepository;
    private final FriendEntityRepository friendEntityRepository;

    private List<UserDomain> friendsList;

    private final LoggedUserService loggedUserService;


    public List<UserDomain> getFriendsList() {
        return getFriends(loggedUserService.getUser().getUsername());
    }

    @Override
    public List<UserDomain> getFriends(String user) {
        List<UserDomain> userDomainList = new ArrayList<>();

        for (FriendEntity friendEntity : friendEntityRepository.findAllByUser1Username(user)) {
            UserEntity userEntity = friendEntity.getUser2();

            Set<GrantedAuthority> authorities = new HashSet<>();

            for (RoleEntity currentRole : userEntity.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(Utils.toSpringRole(currentRole)));
            }

            userDomainList.add(new UserDomain(
                    userEntity.getId(),
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.getFirstName(),
                    userEntity.getLastName(),
                    authorities
            ));

        }

        return Collections.unmodifiableList(userDomainList);
    }

    @Override
    public boolean areFriends(String username1, String username2) {
        for (FriendEntity friendEntity : friendEntityRepository.findAllByUser1Username(username1)) {
            UserEntity userEntity = friendEntity.getUser2();

            if (userEntity.getUsername().equals(username2)) {
                return true;
            }

        }

        return false;
    }

    @Override
    public void addFriend(UserDomain friend) {
        addFriend(loggedUserService.getUser(), friend);
    }

    @Transactional
    @Override
    public void addFriend(UserDomain loggedUser, UserDomain friend) {
        List<UserDomain> friendList1 = getFriends(loggedUser.getUsername());
        List<UserDomain> friendList2 = getFriends(friend.getUsername());

        if (loggedUser.equals(friend)) {
            log.error("It is not possible to be friend with your own.");
            return;
        }

        if (friendList1.contains(friend) || friendList2.contains(loggedUser)) {
            log.error("Users " + loggedUser.getUsername() + " and " + friend.getUsername() + " are already friends.");
            return;
        }

        UserEntity loggedUserEntity = userEntityRepository.findUserEntityByUsername(loggedUser.getUsername());
        UserEntity friendUserEntity = userEntityRepository.findUserEntityByUsername(friend.getUsername());


        FriendEntity friendEntity1 = new FriendEntity(loggedUserEntity, friendUserEntity);
        FriendEntity friendEntity2 = new FriendEntity(friendUserEntity, loggedUserEntity);

        friendEntityRepository.save(friendEntity1);
        friendEntityRepository.save(friendEntity2);
    }

    @Override
    public void removeFriend(UserDomain friend) {
        removeFriend(loggedUserService.getUser(), friend);
    }

    @Transactional
    public void removeFriend(UserDomain loggedUser, UserDomain friend) {
        List<UserDomain> friendList1 = getFriends(loggedUser.getUsername());

        if (!friendList1.contains(friend)) {
            log.error("Required user is not in friend list.");
        }

        List<FriendEntity> loggedUserEntities = friendEntityRepository.findAllByUser1UsernameAndUser2Username(loggedUser.getUsername(), friend.getUsername());
        List<FriendEntity> friendEntities = friendEntityRepository.findAllByUser1UsernameAndUser2Username(friend.getUsername(), loggedUser.getUsername());

        for (FriendEntity friendEntity : loggedUserEntities) {
            friendEntityRepository.delete(friendEntity);
        }

        for (FriendEntity friendEntity : friendEntities) {
            friendEntityRepository.delete(friendEntity);
        }

    }
}
