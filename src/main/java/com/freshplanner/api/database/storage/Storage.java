package com.freshplanner.api.database.storage;

import com.freshplanner.api.database.user.User;
import com.freshplanner.api.model.storage.StorageModel;
import com.freshplanner.api.model.storage.StorageSummaryModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Storage")
@Table(name = "storages")
public class Storage implements Serializable {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "storage", orphanRemoval = true)
    private final Set<StorageItem> storageItems = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_storages",
            joinColumns = {@JoinColumn(name = "storage_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "name")})
    private Set<User> users = new HashSet<>();

    public Storage(User user, StorageSummaryModel storage) {
        this.name = storage.getName();
        this.users.add(user);
    }

    public Storage update(StorageModel storageModel) {
        if (storageModel.getName() != null) {
            this.name = storageModel.getName();
        }
        return this;
    }

    public Storage addUser(User user) {
        users.add(user);
        return this;
    }

    public Storage removeUser(User user) {
        users.remove(user);
        return this;
    }

    public boolean containsUser(String username) {
        Optional<User> matchingUser = users.stream().
                filter(p -> p.getName().equals(username)).
                findFirst();
        return matchingUser.isPresent();
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Storage storage = (Storage) o;

        if (!Objects.equals(id, storage.id)) return false;
        return Objects.equals(name, storage.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
