package com.freshplanner.api.database.storage;

import com.freshplanner.api.database.product.ProductDB;
import com.freshplanner.api.database.user.User;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.storage.StorageModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public record StorageDB(StorageRepo storageRepo,
                        StorageItemRepo storageItemRepo,
                        ProductDB productSelector) {

    /**
     * SELECT
     *
     * @param storageId database id
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     */
    public Storage getStorageById(Integer storageId) throws ElementNotFoundException {
        Optional<Storage> storage = storageRepo.findById(storageId);
        if (storage.isPresent()) {
            return storage.get();
        } else {
            throw new ElementNotFoundException(Storage.class, storageId.toString());
        }
    }

    /**
     * SELECT
     *
     * @param username for identification
     * @return result of associated storages
     */
    public List<Storage> getUserStorages(String username) {
        return storageRepo.findStorageByUsername(username);
    }

    /**
     * SELECT
     *
     * @param storageId for identification
     * @param username  to validate
     * @throws AccessDeniedException if authentication is not associated
     */
    public void validateUserForStorage(Integer storageId, String username) throws AccessDeniedException {
        Optional<Integer> resultId = storageRepo.findStorageByIdAndUser(storageId, username);
        if (resultId.isEmpty() || !resultId.get().equals(storageId)) {
            throw new AccessDeniedException(username + " has no access for storage " + resultId);
        }
    }

    /**
     * SELECT
     *
     * @param storageId associated storage id
     * @param productId associated product id
     * @return result object
     * @throws ElementNotFoundException if composite id does not exist
     */
    private StorageItem getStorageItemById(Integer storageId, Integer productId) throws ElementNotFoundException {
        StorageItem.Key id = new StorageItem.Key(storageId, productId);
        Optional<StorageItem> item = storageItemRepo.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ElementNotFoundException(StorageItem.class, id.toString());
        }
    }

    /**
     * INSERT
     *
     * @param user associated authentication
     * @return created object
     */
    public Storage addStorage(User user) {
        return storageRepo.save(new Storage(user));
    }

    /**
     * INSERT
     *
     * @param modification with input data
     * @return created object
     * @throws ElementNotFoundException if associated id does not exist
     */
    public StorageItem addStorageItem(int storageId, StorageModel.Item modification) throws ElementNotFoundException {
        StorageItem item = new StorageItem(this.getStorageById(storageId),
                productSelector.getProductById(modification.getProductId()),
                modification.getCount());
        return storageItemRepo.save(item);
    }

    /**
     * UPDATE
     *
     * @param modification with input data
     * @return updated object
     * @throws ElementNotFoundException if associated id does not exist
     */
    public StorageItem updateStorageItem(int storageId, StorageModel.Item modification) throws ElementNotFoundException {
        StorageItem item = this.getStorageItemById(storageId, modification.getProductId());
        return storageItemRepo.save(item.setCount(modification.getCount()));
    }

    /**
     * DELETE
     *
     * @param storageId associated storage id
     * @param productId associated product id
     * @return deleted object
     * @throws ElementNotFoundException if associated id does not exist
     */
    public StorageItem deleteStorageItem(Integer storageId, Integer productId) throws ElementNotFoundException {
        StorageItem item = this.getStorageItemById(storageId, productId);
        storageItemRepo.delete(item);
        return item;
    }

    /**
     * DELETE
     *
     * @param storageId database id
     * @return deleted object
     * @throws ElementNotFoundException if id does not exist
     */
    public Storage deleteStorageById(Integer storageId) throws ElementNotFoundException {
        Storage storage = this.getStorageById(storageId);
        storageRepo.delete(storage);
        return storage;
    }
}
