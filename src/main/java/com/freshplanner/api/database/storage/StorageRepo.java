package com.freshplanner.api.database.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface StorageRepo extends JpaRepository<Storage, Integer> {

    @Query(value = "select * from storages s join user_storages us on s.id = us.storage_id where us.user_id = :username", nativeQuery = true)
    List<Storage> findStorageByUsername(@Param("username") String username);

    @Query(value = "select distinct s.storage_id from user_storages s where s.user_id = :username and s.storage_id = :id", nativeQuery = true)
    Optional<Integer> findStorageByIdAndUser(@Param("id") Integer id, @Param("username") String username);
}
