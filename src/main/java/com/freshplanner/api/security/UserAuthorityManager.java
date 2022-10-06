package com.freshplanner.api.security;

import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAuthorityManager implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserAuthorityManager(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param username for DB (case-insensitive)
     * @return {@link UserAuthority}
     * @throws UsernameNotFoundException if username does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return UserAuthority.build(userService.getUserByName(username));
        } catch (ElementNotFoundException e) {
            throw new UsernameNotFoundException(e.getId() + " not found in " + e.getTable());
        }
    }
}
