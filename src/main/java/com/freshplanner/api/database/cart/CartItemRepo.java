package com.freshplanner.api.database.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CartItemRepo extends JpaRepository<CartItem, CartItem.Key> {
}
