package com.freshplanner.api.controller;

import com.freshplanner.api.controller.model.User;
import com.freshplanner.api.controller.model.authentication.AuthModel;
import com.freshplanner.api.controller.model.authentication.LoginModel;
import com.freshplanner.api.controller.model.authentication.RegistrationModel;
import com.freshplanner.api.enums.RoleName;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.security.JwtManager;
import com.freshplanner.api.security.SecurityContext;
import com.freshplanner.api.service.user.UserDB;
import com.freshplanner.api.service.user.UserEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserDB userDB;
    private final AuthenticationManager authenticationManager;
    private final JwtManager jwtManager;
    @Value("${app.settings.jwt.type}")
    private String jwtType;

    @Autowired
    public AuthenticationController(UserDB userDB, AuthenticationManager authenticationManager, JwtManager jwtManager) {
        this.userDB = userDB;
        this.authenticationManager = authenticationManager;
        this.jwtManager = jwtManager;
    }

    // === POST ========================================================================================================

    /**
     * Creates a JWT with the credentials and sets the authority into the SecurityContext.
     *
     * @param request {@link LoginModel} with the credentials to create to JWT
     * @return {@link AuthModel} with the token and additional information
     */
    @ApiOperation("Login for access to the API.")
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthModel> login(@Valid @RequestBody LoginModel request) throws ElementNotFoundException {
        UserEntity userModel = userDB.getUserByName(request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtManager.generateJwtToken(authentication);

        return ResponseEntity.ok(new AuthModel(userModel, jwt, jwtType));
    }

    @ApiOperation("Register for access to the API.")
    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthModel> register(@Valid @RequestBody RegistrationModel request) throws ElementNotFoundException {
        userDB.addUser(request);
        return login(new LoginModel(request.getUsername(), request.getPassword()));
    }

    // === GET =========================================================================================================

    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get the userinfo about this authentication.")
    @GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserInfo() throws ElementNotFoundException {
        return ResponseEntity.ok(userDB.getUserByName(SecurityContext.extractUsername()).mapToModel());
    }

    @ApiOperation("Get all possible values of: Auth Database - Roles")
    @GetMapping(path = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleName>> getAuthRoles() {
        return ResponseEntity.ok(RoleName.getAll());
    }

    @ApiOperation("Get all possible values of: Auth Database - Roles")
    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getUsers() {
        return ResponseEntity.ok(userDB.getAllUsernames());
    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Delete authentication by name.")
    @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUser() throws ElementNotFoundException {
        return ResponseEntity.ok(userDB.deleteUserById(SecurityContext.extractUsername()).mapToModel());
    }
}
