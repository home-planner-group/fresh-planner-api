package com.freshplanner.api.database.storage;

import com.freshplanner.api.database.product.ProductDB;
import com.freshplanner.api.database.user.User;
import com.freshplanner.api.database.user.UserDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.NoAccessException;
import com.freshplanner.api.model.storage.StorageModel;
import com.freshplanner.api.model.storage.StorageSummaryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class StorageDB {

    private final StorageRepo storageRepo;
    private final StorageItemRepo storageItemRepo;
    private final ProductDB productSelector;
    private final UserDB userDB;

    @Autowired
    public StorageDB(StorageRepo storageRepo, StorageItemRepo storageItemRepo, ProductDB productSelector, UserDB userDB) {
        this.storageRepo = storageRepo;
        this.storageItemRepo = storageItemRepo;
        this.productSelector = productSelector;
        this.userDB = userDB;
    }

    public Storage getStorageById(String username, Integer storageId) throws ElementNotFoundException, NoAccessException {
        Storage storage = getStorageById(storageId);
        if (storage.containsUser(username)) {
            return storage;
        } else {
            throw new NoAccessException(username, Storage.class, storageId.toString());
        }
    }

    private Storage getStorageById(Integer storageId) throws ElementNotFoundException {
        Optional<Storage> storage = storageRepo.findById(storageId);
        if (storage.isPresent()) {
            return storage.get();
        } else {
            throw new ElementNotFoundException(Storage.class, storageId.toString());
        }
    }

    public List<Storage> getUserStorages(String username) {
        return storageRepo.findStorageByUsername(username);
    }

    private StorageItem getStorageItemById(Integer storageId, Integer productId) throws ElementNotFoundException {
        StorageItem.Key id = new StorageItem.Key(storageId, productId);
        Optional<StorageItem> item = storageItemRepo.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ElementNotFoundException(StorageItem.class, id.toString());
        }
    }

    @Transactional
    public Storage addStorage(String username, StorageSummaryModel storageModel) throws ElementNotFoundException {
        User user = userDB.getUserByName(username);
        return storageRepo.save(new Storage(user, storageModel));
    }

    @Transactional
    public Storage addStorageItem(String username, int storageId, StorageModel.Item storageItemModel) throws ElementNotFoundException, NoAccessException {
        Storage storage = getStorageById(username, storageId);
        StorageItem item = storageItemRepo.save(new StorageItem(
                storage,
                productSelector.getProductById(storageItemModel.getProductId()),
                storageItemModel.getCount()));
        storage.getStorageItems().add(item);
        return storage;
    }

    // TODO implement updateStorageItem with PUT request
    public StorageItem updateStorageItem(int storageId, StorageModel.Item modification) throws ElementNotFoundException {
        StorageItem item = this.getStorageItemById(storageId, modification.getProductId());
        return storageItemRepo.save(item.setCount(modification.getCount()));
    }

    public Storage deleteStorageItem(String username, Integer storageId, Integer productId) throws ElementNotFoundException, NoAccessException {
        Storage storage = this.getStorageById(username, storageId);
        StorageItem item = this.getStorageItemById(storageId, productId);
        storageItemRepo.delete(item);
        storage.getStorageItems().remove(item);
        return storage;
    }

    public Storage deleteStorageById(String username, Integer storageId) throws ElementNotFoundException, NoAccessException {
        Storage storage = this.getStorageById(username, storageId);
        storageRepo.delete(storage);
        return storage;
    }
}
