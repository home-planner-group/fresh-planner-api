package com.freshplanner.api.service.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CartItemRepo extends JpaRepository<CartItem, CartItem.Key> {
}
