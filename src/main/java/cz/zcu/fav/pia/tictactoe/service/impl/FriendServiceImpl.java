package cz.zcu.fav.pia.tictactoe.service.impl;

import cz.zcu.fav.pia.tictactoe.domain.UserDomain;
import cz.zcu.fav.pia.tictactoe.entity.FriendEntity;
import cz.zcu.fav.pia.tictactoe.entity.RoleEntity;
import cz.zcu.fav.pia.tictactoe.entity.UserEntity;
import cz.zcu.fav.pia.tictactoe.repository.FriendEntityRepository;
import cz.zcu.fav.pia.tictactoe.repository.UserEntityRepository;
import cz.zcu.fav.pia.tictactoe.service.FriendService;
import cz.zcu.fav.pia.tictactoe.util.LoggedUserService;
import cz.zcu.fav.pia.tictactoe.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.transaction.Transactional;
import java.util.*;


@Service("friendService")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("isAuthenticated()")
@RequestScope
public class FriendServiceImpl implements FriendService {

    private final UserEntityRepository userEntityRepository;
    private final FriendEntityRepository friendEntityRepository;

    private List<UserDomain> friendsList;

    private final LoggedUserService loggedUserService;

    public List<UserDomain> getFriendsList() {
        //TODO: možná disable cache
        if (this.friendsList == null) {
            this.friendsList = getFriends(loggedUserService.getUser());
        }

        return friendsList;
    }

    private List<UserDomain> getFriends(UserDomain user) {
        List<UserDomain> userDomainList = new ArrayList<>();

        for (FriendEntity friendEntity : friendEntityRepository.findAllByUser1Username(user.getUsername())) {
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

    public void addFriend(UserDomain friend) {
        addFriend(loggedUserService.getUser(), friend);
    }

    @Transactional
    public void addFriend(UserDomain loggedUser, UserDomain friend) {
        List<UserDomain> friendList1 = getFriends(loggedUser);
        List<UserDomain> friendList2 = getFriends(friend);

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

    public void removeFriend(UserDomain friend) {
        removeFriend(loggedUserService.getUser(), friend);
    }

    @Transactional
    public void removeFriend(UserDomain loggedUser, UserDomain friend) {
        List<UserDomain> friendList1 = getFriends(loggedUser);

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
