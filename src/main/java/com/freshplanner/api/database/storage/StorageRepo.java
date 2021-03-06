package com.freshplanner.api.database.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface StorageRepo extends JpaRepository<Storage, Integer> {

    // native query because of non-case-sensitivity on the username
    @Query(value = "select * from storages s join user_storages us on s.id = us.storage_id where us.user_id = :username", nativeQuery = true)
    List<Storage> findStoragesByUsername(@Param("username") String username);
}
