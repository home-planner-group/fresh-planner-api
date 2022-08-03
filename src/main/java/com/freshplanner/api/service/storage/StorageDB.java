package com.freshplanner.api.service.storage;

import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.NoAccessException;
import com.freshplanner.api.model.storage.StorageModel;
import com.freshplanner.api.model.storage.StorageSummaryModel;
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
    public Storage selectStorageById(String username, Integer storageId) throws ElementNotFoundException, NoAccessException {
        Storage storage = selectStorageById(storageId);
        if (storage.containsUser(username)) {
            return storage;
        } else {
            throw new NoAccessException(username, Storage.class, storageId.toString());
        }
    }

    private Storage selectStorageById(Integer storageId) throws ElementNotFoundException {
        Optional<Storage> storage = storageRepo.findById(storageId);
        if (storage.isPresent()) {
            return storage.get();
        } else {
            throw new ElementNotFoundException(Storage.class, storageId.toString());
        }
    }

    /**
     * SELECT storage WHERE user
     *
     * @param username as owner
     * @return list with result objects
     */
    public List<Storage> selectUserStorages(String username) {
        return storageRepo.findStoragesByUsername(username);
    }

    private StorageItem selectStorageItemById(Integer storageId, Integer productId) throws ElementNotFoundException {
        StorageItem.Key id = new StorageItem.Key(storageId, productId);
        Optional<StorageItem> item = storageItemRepo.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ElementNotFoundException(StorageItem.class, id.toString());
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
    public Storage insertStorage(String username, StorageSummaryModel storageModel) throws ElementNotFoundException {
        User user = userDB.getUserByName(username);
        return storageRepo.save(new Storage(user, storageModel));
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
    public Storage insertStorageItem(String username, int storageId, StorageModel.Item storageItemModel) throws ElementNotFoundException, NoAccessException {
        Storage storage = selectStorageById(username, storageId);
        StorageItem item = storageItemRepo.save(new StorageItem(
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
    public Storage updateAddUser(String usernameOwner, int storageId, String usernameMember) throws ElementNotFoundException, NoAccessException {
        Storage storage = selectStorageById(usernameOwner, storageId);
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
    public Storage updateRemoveUser(String usernameOwner, int storageId, String usernameMember) throws ElementNotFoundException, NoAccessException {
        Storage storage = selectStorageById(usernameOwner, storageId);
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
    public Storage updateStorage(String username, StorageModel storageModel) throws NoAccessException, ElementNotFoundException {
        Storage storage = this.selectStorageById(username, storageModel.getId());
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
    public StorageItem updateStorageItem(String username, Integer storageId, StorageModel.Item itemModel) throws ElementNotFoundException, NoAccessException {
        StorageItem item = this.selectStorageItemById(storageId, itemModel.getProductId());
        if (!item.getStorage().containsUser(username)) {
            throw new NoAccessException(username, Storage.class, storageId.toString());
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
    public Storage deleteStorageItem(String username, Integer storageId, Integer productId) throws ElementNotFoundException, NoAccessException {
        Storage storage = this.selectStorageById(username, storageId);
        StorageItem item = this.selectStorageItemById(storageId, productId);
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
    public Storage deleteStorageById(String username, Integer storageId) throws ElementNotFoundException, NoAccessException {
        Storage storage = this.selectStorageById(username, storageId);
        storageRepo.delete(storage);
        return storage;
    }
}
