package environment;

import com.freshplanner.api.Application;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.authentication.RegistrationModel;
import com.freshplanner.api.service.user.User;
import com.freshplanner.api.service.user.UserDB;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utility.DataFactory;
import utility.TestLogger;

import static com.freshplanner.api.enums.RoleName.ROLE_ADMIN;
import static utility.AssertionUtils.assertEquals;

/**
 * Environment for all application tests. Enables the @Order-Annotation and the given Spring Context.
 * Creates a user for tests with authentication.
 */
@SpringBootTest(classes = {Application.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ApplicationTest {

    protected User user;
    @Autowired
    private UserDB userDB;

    @BeforeAll
    protected void generateAdminUser() throws ElementNotFoundException {
        RegistrationModel registrationModel = DataFactory.User.registration();
        TestLogger.info("Model for operation: " + registrationModel);

        User userActual = userDB.addUser(registrationModel);
        assertEquals(registrationModel.getUsername(), userActual.getName());

        user = userDB.addRoleToUser(userActual.getName(), ROLE_ADMIN);
        TestLogger.info("Inserted user: " + user);
    }

    @AfterAll
    protected void deleteAdminUser() throws ElementNotFoundException {
        User userActual = userDB.deleteUserById(user.getName());
        assertEquals(user, userActual);

        user = userActual;
        TestLogger.info("Deleted user: " + user);
    }
}
