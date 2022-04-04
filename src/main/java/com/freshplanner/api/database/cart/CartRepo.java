package com.freshplanner.api.database.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface CartRepo extends JpaRepository<Cart, Integer> {

    // native query because of non-case-sensitivity on the username
    @Query(value = "select * from carts c join user_carts uc on c.id = uc.cart_id where uc.user_id = :username", nativeQuery = true)
    List<Cart> findCartsByUsername(@Param("username") String username);
}
