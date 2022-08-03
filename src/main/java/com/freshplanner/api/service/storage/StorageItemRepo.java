package com.freshplanner.api.service.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface StorageItemRepo extends JpaRepository<StorageItem, StorageItem.Key> {
}
