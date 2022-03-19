package com.freshplanner.api.controller;

import com.freshplanner.api.database.enums.RoleName;
import com.freshplanner.api.database.user.Role;
import com.freshplanner.api.database.user.User;
import com.freshplanner.api.database.user.UserDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.authentication.LoginModel;
import com.freshplanner.api.model.authentication.RegistrationModel;
import com.freshplanner.api.model.authentication.UserAuthModel;
import com.freshplanner.api.model.authentication.UserInfoModel;
import com.freshplanner.api.security.JwtManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
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
     * @return {@link UserAuthModel} with the token and additional information
     */
    @ApiOperation("Login for access to the API.")
    @PostMapping(path = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAuthModel> login(@Valid @RequestBody LoginModel request) throws ElementNotFoundException {
        User userModel = userDB.getUserByName(request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtManager.generateJwtToken(authentication);

        return ResponseEntity.ok(new UserAuthModel(userModel, jwt, jwtType));
    }

    @ApiOperation("Register for access to the API.")
    @PostMapping(path = "/auth/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAuthModel> register(@Valid @RequestBody RegistrationModel request) throws ElementNotFoundException {
        userDB.addUser(request);
        return login(new LoginModel(request.getUsername(), request.getPassword()));
    }

    // === GET =========================================================================================================

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Get the userinfo about this authentication.")
    @GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoModel> getUserInfo(
            @ApiParam(value = "Username for details.", example = "MaxMastermind", required = true)
            @RequestParam("name") String username) throws ElementNotFoundException {
        return ResponseEntity.ok(new UserInfoModel(userDB.getUserByName(username)));
    }

    @ApiOperation("Get all possible values of: Auth Database - Roles")
    @GetMapping(path = "/roles/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleName>> getAuthRoles() {
        return ResponseEntity.ok(userDB.getAllRoles()
                .stream().map(Role::getName).collect(Collectors.toList()));
    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Delete authentication by name.")
    @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoModel> deleteUser(
            @ApiParam(value = "Username to delete.", example = "Max")
            @RequestParam("name") String username)
            throws ElementNotFoundException {

        return ResponseEntity.ok(new UserInfoModel(userDB.deleteUserById(username)));
    }
}
