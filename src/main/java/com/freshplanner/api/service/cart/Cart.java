package com.freshplanner.api.service.cart;

import com.freshplanner.api.model.cart.CartModel;
import com.freshplanner.api.model.cart.CartSummaryModel;
import com.freshplanner.api.service.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Cart")
@Table(name = "carts")
public class Cart {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart", orphanRemoval = true)
    private final Set<CartItem> cartItems = new HashSet<>();

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
    private Set<User> users = new HashSet<>();

    public Cart(User user, CartSummaryModel model) {
        this.name = model.getName();
        this.users.add(user);
    }

    public Cart update(CartModel model) {
        if (model.getName() != null) {
            this.name = model.getName();
        }
        return this;
    }

    public Cart addUser(User user) {
        users.add(user);
        return this;
    }

    public Cart removeUser(User user) {
        users.remove(user);
        return this;
    }

    public boolean containsUser(String username) {
        Optional<User> matchingUser = users.stream().
                filter(p -> p.getName().equals(username)).
                findFirst();
        return matchingUser.isPresent();
    }
}
