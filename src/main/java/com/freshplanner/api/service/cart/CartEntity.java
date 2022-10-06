package com.freshplanner.api.service.cart;

import com.freshplanner.api.controller.model.Cart;
import com.freshplanner.api.service.user.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Cart")
@Table(name = "carts")
public class CartEntity {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart", orphanRemoval = true)
    private final Set<CartItemEntity> cartItems = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_carts",
            joinColumns = {@JoinColumn(name = "cart_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "name")})
    private Set<UserEntity> users = new HashSet<>();

    public CartEntity(UserEntity user, Cart model) {
        this.name = model.getName();
        this.users.add(user);
    }

    public Cart mapToModel() {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setName(name);
        cart.setUsers(users.stream().map(UserEntity::getName).collect(Collectors.toList()));
        cart.setItems(cartItems.stream().map(CartItemEntity::mapToModel).collect(Collectors.toList()));
        return cart;
    }

    public CartEntity update(Cart model) {
        if (model.getName() != null) {
            this.name = model.getName();
        }
        return this;
    }

    public CartEntity addUser(UserEntity user) {
        users.add(user);
        return this;
    }

    public CartEntity removeUser(UserEntity user) {
        users.remove(user);
        return this;
    }

    public boolean containsUser(String username) {
        Optional<UserEntity> matchingUser = users.stream().
                filter(p -> p.getName().equals(username)).
                findFirst();
        return matchingUser.isPresent();
    }
}
