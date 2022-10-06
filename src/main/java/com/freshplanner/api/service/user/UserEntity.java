package com.freshplanner.api.service.user;

import com.freshplanner.api.controller.model.User;
import com.freshplanner.api.enums.RoleName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "name")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "name")})
    private Set<RoleEntity> roles = new HashSet<>();

    /**
     * Required data for a user.
     */
    public UserEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User mapToModel() {
        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setRoles(roles.stream().map(RoleEntity::getName).collect(Collectors.toList()));
        return user;
    }

    /**
     * Save the modified user to update the {@link JoinTable}.
     *
     * @param role to associate with the user
     * @return modified user
     */
    public UserEntity addRole(RoleName role) {
        this.roles.add(new RoleEntity(role));
        return this;
    }

    /**
     * Save the modified user to update the {@link JoinTable}.
     *
     * @param role to remove from the user
     * @return modified user
     */
    public UserEntity removeRole(RoleName role) {
        this.roles.remove(new RoleEntity(role));
        return this;
    }

    public boolean hasRole(RoleName role) {
        return this.roles.contains(new RoleEntity(role));
    }

    // === OBJECT DEFAULT METHODS ======================================================================================

    /**
     * @return user with public native values
     */
    @Override
    public String toString() {
        return "UserEntity{" +
                "username=" + name +
                ", email=" + email + "}";
    }

    /**
     * @param obj to compare
     * @return true if username matches
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        UserEntity user = (UserEntity) obj;
        return name != null && Objects.equals(name, user.name);
    }

    /**
     * @return hash of the username
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
