package com.freshplanner.api.service.storage;

import com.freshplanner.api.controller.model.Storage;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.NoAccessException;
import com.freshplanner.api.service.product.ProductService;
import com.freshplanner.api.service.user.User;
import com.freshplanner.api.service.user.UserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class StorageDB {

    private final StorageRepo storageRepo;
    private final StorageItemRepo storageItemRepo;
    private final ProductService productService;
    private final UserDB userDB;

    @Autowired
    public StorageDB(StorageRepo storageRepo, StorageItemRepo storageItemRepo, ProductService productService, UserDB userDB) {
        this.storageRepo = storageRepo;
        this.storageItemRepo = storageItemRepo;
        this.productService = productService;
        this.userDB = userDB;
    }

    // === SELECT ======================================================================================================

    /**
     * SELECT storage WHERE storageId
     *
     * @param username  as owner
     * @param storageId database id
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    public StorageEntity selectStorageById(String username, Integer storageId) throws ElementNotFoundException, NoAccessException {
        StorageEntity storage = selectStorageById(storageId);
        if (storage.containsUser(username)) {
            return storage;
        } else {
            throw new NoAccessException(username, StorageEntity.class, storageId.toString());
        }
    }

    private StorageEntity selectStorageById(Integer storageId) throws ElementNotFoundException {
        Optional<StorageEntity> storage = storageRepo.findById(storageId);
        if (storage.isPresent()) {
            return storage.get();
        } else {
            throw new ElementNotFoundException(StorageEntity.class, storageId.toString());
        }
    }

    /**
     * SELECT storage WHERE user
     *
     * @param username as owner
     * @return list with result objects
     */
    public List<StorageEntity> selectUserStorages(String username) {
        return storageRepo.findStoragesByUsername(username);
    }

    private StorageItemEntity selectStorageItemById(Integer storageId, Integer productId) throws ElementNotFoundException {
        StorageItemEntity.Key id = new StorageItemEntity.Key(storageId, productId);
        Optional<StorageItemEntity> item = storageItemRepo.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ElementNotFoundException(StorageItemEntity.class, id.toString());
        }
    }

    // === INSERT ======================================================================================================

    /**
     * INSERT storage WITH user
     *
     * @param username     as owner
     * @param storageModel with input data
     * @return created object
     * @throws ElementNotFoundException if id does not exist
     */
    @Transactional
    public StorageEntity insertStorage(String username, Storage storageModel) throws ElementNotFoundException {
        User user = userDB.getUserByName(username);
        return storageRepo.save(new StorageEntity(user, storageModel));
    }

    /**
     * INSERT storageItem
     *
     * @param username         as owner
     * @param storageId        linked database id
     * @param storageItemModel with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    @Transactional
    public StorageEntity insertStorageItem(String username, int storageId, Storage.Item storageItemModel) throws ElementNotFoundException, NoAccessException {
        StorageEntity storage = selectStorageById(username, storageId);
        StorageItemEntity item = storageItemRepo.save(new StorageItemEntity(
                storage,
                productService.selectProductById(storageItemModel.getProductId()),
                storageItemModel.getCount()));
        storage.getStorageItems().add(item);
        return storage;
    }

    // === UPDATE ======================================================================================================

    /**
     * INSERT user INTO storage
     *
     * @param usernameOwner  as owner
     * @param storageId      database id
     * @param usernameMember new member
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    @Transactional
    public StorageEntity updateAddUser(String usernameOwner, int storageId, String usernameMember) throws ElementNotFoundException, NoAccessException {
        StorageEntity storage = selectStorageById(usernameOwner, storageId);
        User user = userDB.getUserByName(usernameMember);
        return storageRepo.save(storage.addUser(user));
    }

    /**
     * DELETE user FROM storage
     *
     * @param usernameOwner  as owner
     * @param storageId      database id
     * @param usernameMember member to delete
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    @Transactional
    public StorageEntity updateRemoveUser(String usernameOwner, int storageId, String usernameMember) throws ElementNotFoundException, NoAccessException {
        StorageEntity storage = selectStorageById(usernameOwner, storageId);
        User user = userDB.getUserByName(usernameMember);
        return storageRepo.save(storage.removeUser(user));
    }

    /**
     * UPDATE storage
     *
     * @param username     as owner
     * @param storageModel with input data
     * @return updated object
     * @throws NoAccessException        if user is no owner
     * @throws ElementNotFoundException if id does not exist
     */
    public StorageEntity updateStorage(String username, Storage storageModel) throws NoAccessException, ElementNotFoundException {
        StorageEntity storage = this.selectStorageById(username, storageModel.getId());
        return storageRepo.save(storage.update(storageModel));
    }

    /**
     * UPDATE storageItem
     *
     * @param username  as owner
     * @param storageId linked database id
     * @param itemModel with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    public StorageItemEntity updateStorageItem(String username, Integer storageId, Storage.Item itemModel) throws ElementNotFoundException, NoAccessException {
        StorageItemEntity item = this.selectStorageItemById(storageId, itemModel.getProductId());
        if (!item.getStorage().containsUser(username)) {
            throw new NoAccessException(username, StorageEntity.class, storageId.toString());
        }
        return storageItemRepo.save(item.update(itemModel));
    }

    // === DELETE ======================================================================================================

    /**
     * DELETE storageItem WHERE storageId and productId
     *
     * @param username  as owner
     * @param storageId linked database id
     * @param productId linked database id
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    public StorageEntity deleteStorageItem(String username, Integer storageId, Integer productId) throws ElementNotFoundException, NoAccessException {
        StorageEntity storage = this.selectStorageById(username, storageId);
        StorageItemEntity item = this.selectStorageItemById(storageId, productId);
        storageItemRepo.delete(item);
        storage.getStorageItems().remove(item);
        return storage;
    }

    /**
     * DELETE storage WHERE storageId
     *
     * @param username  as owner
     * @param storageId database id
     * @return deleted object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    public StorageEntity deleteStorageById(String username, Integer storageId) throws ElementNotFoundException, NoAccessException {
        StorageEntity storage = this.selectStorageById(username, storageId);
        storageRepo.delete(storage);
        return storage;
    }
}
