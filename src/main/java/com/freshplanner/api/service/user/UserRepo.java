package com.freshplanner.api.service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface UserRepo extends JpaRepository<User, String> {
    /**
     * @param username case-insensitive query because of nativeQuery
     * @return optional {@link User}
     */
    @Query(value = "select * from users u where u.name = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "select distinct u.name from User u")
    List<String> getAllUsernames();
}
