package environment;

import com.freshplanner.api.Application;
import com.freshplanner.api.database.user.UserDB;
import com.freshplanner.api.model.authentication.RegistrationModel;
import com.freshplanner.api.model.authentication.UserAuthModel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import utility.DataFactory;
import utility.JsonFactory;
import utility.TestLogger;

import static com.freshplanner.api.database.enums.RoleName.ROLE_ADMIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utility.AssertionUtils.assertNotNull;

/**
 * Environment for all controller tests. Enables the @Order-Annotation and the given Spring Context.
 * Creates a user for tests with authentication.
 */
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest(classes = {Application.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    protected UserAuthModel userAuth;
    @Autowired
    private UserDB userDB;

    @BeforeAll
    protected void generateAdminUser() throws Exception {
        RegistrationModel registrationModel = DataFactory.User.registration();
        TestLogger.info("Model for operation: " + registrationModel);
        String modelJson = JsonFactory.convertToJson(registrationModel);
        TestLogger.info("Json for operation: " + modelJson);

        MvcResult result = mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(modelJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        UserAuthModel userAuthActual = JsonFactory.convertToObject(result, UserAuthModel.class);
        assertNotNull(userAuthActual);
        assertNotNull(userAuthActual.getJwt());

        userAuth = userAuthActual;
        TestLogger.info("Registered user: " + userAuth);
        userDB.addRoleToUser(userAuth.getUsername(), ROLE_ADMIN);
        TestLogger.info("Added admin role to: " + userAuth.getUsername());
    }

    @AfterAll
    protected void deleteAdminUser() throws Exception {
        mockMvc.perform(
                        delete("/auth/delete")
                                .header(HttpHeaders.AUTHORIZATION, userAuth.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        TestLogger.info("Deleted user: " + userAuth);
    }
}
