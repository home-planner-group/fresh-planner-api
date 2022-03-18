package com.freshplanner.api.database.storage;

import com.freshplanner.api.database.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity(name = "Storage")
@Table(name = "storages")
public class Storage implements Serializable {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "storage", orphanRemoval = true)
    private final Set<StorageItem> storageItems = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_storages",
            joinColumns = {@JoinColumn(name = "storage_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "name")})
    private Set<User> users = new HashSet<>();

    public Storage(User user) {
        this.users.add(user);
    }

    public Storage addUser(User user) {
        users.add(user);
        return this;
    }

    public Storage removeUser(User user) {
        users.remove(user);
        return this;
    }

    // === OBJECT DEFAULT METHODS ======================================================================================

    @Override
    public String toString() {
        return "Storage{id=" + id + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        Storage storage = (Storage) obj;
        return id.equals(storage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
