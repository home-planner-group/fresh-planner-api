package com.freshplanner.api.service.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface CartRepo extends JpaRepository<CartEntity, Integer> {

    // native query because of non-case-sensitivity on the username
    @Query(value = "select * from carts c join user_carts uc on c.id = uc.cart_id where uc.user_id = :username", nativeQuery = true)
    List<CartEntity> findCartsByUsername(@Param("username") String username);
}
