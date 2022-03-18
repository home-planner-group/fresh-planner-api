package com.freshplanner.api.configuration;

import com.freshplanner.api.security.JwtEntryPointHandler;
import com.freshplanner.api.security.JwtRequestFilter;
import com.freshplanner.api.security.UserAuthorityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api/auth/**"
    };
    @Autowired
    private JwtEntryPointHandler unauthorizedHandler;

    @Autowired
    private UserAuthorityManager userAuthorityManager;

    /**
     * This method is only used to deactivate the entire authentication over the endpoints. (only for development)
     *
     * @param web {@link WebSecurity} has more priority than {@link HttpSecurity}.
     */
    @Override
    public void configure(WebSecurity web) {
        // if (DISABLE_SECURITY) {
        //     web.ignoring().antMatchers("/**");
        //}
    }

    /**
     * <h2>Configuration of the Spring Security</h2>
     * <ol>
     *     <li>Disable CORS (CrossOrigin) and CSRF.</li>
     *     <li>Add {@link JwtEntryPointHandler} as ExceptionHandler for the EntryPoints.</li>
     *     <li>Make the sessions {@link SessionCreationPolicy#STATELESS} to not store any of the user's state.</li>
     *     <li>Set up the not authenticated paths, these are defined in {@link SecurityConfig#AUTH_WHITELIST}.</li>
     *     <li>All other requests need to be authenticated if configuration is set.</li>
     *     <li>Add the {@link JwtRequestFilter} to validate the tokens with every request.</li>
     * </ol>
     *
     * @param http {@link HttpSecurity} to add the configuration. Is on a lower level than {@link WebSecurity}.
     * @throws Exception from overwritten method
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
        // if (SECURE_ALL_ENDPOINTS) {
        //    http.authorizeRequests().anyRequest().authenticated();
        // }
        http.addFilterBefore(authenticationJwtTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Configure {@link AuthenticationManagerBuilder} so that it knows where to load user for matching credentials.
     *
     * @param authenticationManagerBuilder {@link AuthenticationManagerBuilder} to configure
     * @throws Exception from overwritten method
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userAuthorityManager).passwordEncoder(passwordEncoderBean());
    }

    //==================================================================================================================

    /**
     * Enables access to the {@link JwtRequestFilter} via '@Autowired'.
     *
     * @return {@link JwtRequestFilter} to parse every request and set the 'Authentication' for the 'Security Context'.
     */
    @Bean
    public JwtRequestFilter authenticationJwtTokenFilterBean() {
        return new JwtRequestFilter();
    }

    /**
     * Enables access to the {@link AuthenticationManager} via '@Autowired'.
     *
     * @return {@link AuthenticationManager} to generate 'Authentication' for the 'Security Context' and the generated JWT.
     * @throws Exception from overwritten method
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Enables access to the {@link PasswordEncoder} via '@Autowired'.
     *
     * @return {@link PasswordEncoder} for not saving plain text.
     */
    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }
}
