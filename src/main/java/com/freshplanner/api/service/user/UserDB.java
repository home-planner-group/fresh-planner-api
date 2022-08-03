package com.freshplanner.api.service.user;

import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.InvalidPasswordException;
import com.freshplanner.api.model.authentication.RegistrationModel;
import com.freshplanner.api.service.enums.RoleName;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public record UserDB(RoleRepo roleRepo, UserRepo userRepo, PasswordEncoder encoder) {

    // === SELECT ======================================================================================================

    /**
     * @param username for DB (case-insensitive)
     * @return {@link User} from DB
     * @throws ElementNotFoundException if username does not exist
     */
    public User getUserByName(String username) throws ElementNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new ElementNotFoundException(User.class, username);
        }
    }

    /**
     * @return {@link List} of all available {@link Role}s
     */
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    public List<String> getAllUsernames() {
        return userRepo.getAllUsernames();
    }

    // === INSERT ======================================================================================================

    /**
     * Creates a new authentication in the DB with {@link RoleName#ROLE_USER} as default.
     * TODO password should be encoded on client side for more security
     *
     * @param request {@link RegistrationModel} with all required information
     * @return new authentication with default role
     */
    public User addUser(RegistrationModel request) {
        User user = new User(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()));
        user.addRole(RoleName.ROLE_USER);
        return userRepo.save(user);
    }

    // === UPDATE ======================================================================================================

    /**
     * @param username for DB (case-insensitive)
     * @param role     to associate with the authentication
     * @return updated authentication
     * @throws ElementNotFoundException if username does not exist
     */
    public User addRoleToUser(String username, RoleName role) throws ElementNotFoundException {
        return userRepo.save(this.getUserByName(username).addRole(role));
    }

    /**
     * @param username for DB (case-insensitive)
     * @param role     to remove from the authentication
     * @return updated authentication
     * @throws ElementNotFoundException if username does not exist
     */
    public User removeRoleFromUser(String username, RoleName role) throws ElementNotFoundException {
        return userRepo.save(this.getUserByName(username).removeRole(role));
    }

    // === DELETE ======================================================================================================

    /**
     * @param username for DB (case-insensitive)
     * @return removed authentication
     * @throws ElementNotFoundException if username does not exist
     */
    public User deleteUserById(String username) throws ElementNotFoundException {
        User user = this.getUserByName(username);
        userRepo.delete(user);
        return user;
    }

    // === UTILITY =====================================================================================================

    /**
     * @param username for DB (case-insensitive)
     * @param password for validation (encoded)
     * @return {@link User} from DB
     * @throws InvalidPasswordException if password validation failed
     * @throws ElementNotFoundException if username does not exist
     */
    private User validateCredentials(String username, String password) throws ElementNotFoundException, InvalidPasswordException {
        User user = this.getUserByName(username);
        if (password.equals(user.getPassword()) || encoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new InvalidPasswordException("Invalid password for authentication: " + user.getName());
        }
    }
}
