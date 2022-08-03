package com.freshplanner.api.service.user;

import com.freshplanner.api.enums.RoleName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * <h2>RoleName Entity</h2>
 * Table-Data will be initialized through 'src/main/resources/data.sql'.
 */
@NoArgsConstructor
@Getter
@Entity(name = "Role")
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleName name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    /**
     * Required data for a role.
     */
    public Role(RoleName name) {
        this.name = name;
    }

    // === OBJECT DEFAULT METHODS ======================================================================================

    /**
     * @return role with name
     */
    @Override
    public String toString() {
        return "Role{name=" + name + "}";
    }

    /**
     * @param obj to compare
     * @return true of role name matches
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        Role role = (Role) obj;
        return name != null && Objects.equals(name, role.name);
    }

    /**
     * @return hash of the role name
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
