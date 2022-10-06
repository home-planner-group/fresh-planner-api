package com.freshplanner.api.service.storage;

import com.freshplanner.api.controller.model.Storage;
import com.freshplanner.api.service.user.User;
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
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Storage")
@Table(name = "storages")
public class StorageEntity implements Serializable {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "storage", orphanRemoval = true)
    private final Set<StorageItemEntity> storageItems = new HashSet<>();

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

    public StorageEntity(User user, Storage storage) {
        this.name = storage.getName();
        this.users.add(user);
    }

    public Storage mapToModel() {
        Storage storage = new Storage();
        storage.setId(id);
        storage.setName(name);
        storage.setUsers(users.stream().map(User::getName).collect(Collectors.toList()));
        storage.setItems(storageItems.stream().map(StorageItemEntity::mapToModel).collect(Collectors.toList()));
        return storage;
    }

    public StorageEntity update(Storage storageModel) {
        if (storageModel.getName() != null) {
            this.name = storageModel.getName();
        }
        return this;
    }

    public StorageEntity addUser(User user) {
        users.add(user);
        return this;
    }

    public StorageEntity removeUser(User user) {
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
        return "StorageEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageEntity storage = (StorageEntity) o;

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
