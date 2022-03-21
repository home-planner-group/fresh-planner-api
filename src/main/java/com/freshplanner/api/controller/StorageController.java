package com.freshplanner.api.controller;

import com.freshplanner.api.database.enums.RoleName;
import com.freshplanner.api.database.storage.StorageDB;
import com.freshplanner.api.database.user.UserDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.storage.StorageModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageDB storageDB;
    private final UserDB userDB;

    @Autowired
    public StorageController(StorageDB storageDB, UserDB userDB) {
        this.storageDB = storageDB;
        this.userDB = userDB;
    }

    // === GET =========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Get all storages from the user.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StorageModel>> getUserStorages() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(storageDB.getUserStorages(user.getUsername())
                .stream().map(StorageModel::new).collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Get storage by its generated ID.")
    @GetMapping(path = "/get/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> getStorage(
            @ApiParam(value = "storage db id", example = "1") @PathVariable Integer storageId)
            throws ElementNotFoundException {
        return ResponseEntity.ok(new StorageModel(storageDB.getStorageById(storageId)));
    }

    // === POST ========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Create a new storage for the user.")
    @PostMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> addStorage() throws ElementNotFoundException {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new StorageModel(storageDB.addStorage(userDB.getUserByName(user.getUsername()))));
    }

//    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
//    @ApiOperation("Insert a storage item by given parameters. User validation for storage ownership.")
//    @PostMapping(path = "/insert/item", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<StorageModel.Item> addStorageItem(@RequestBody StorageModel.Item item)
//            throws ElementNotFoundException {
//        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        storageDB.validateUserForStorage(item.getStorageId(), user.getUsername());
//        return ResponseEntity.ok(new StorageItemModel(storageDB.addStorageItem(item)));
//    }
//
//    // === PUT =========================================================================================================
//
//    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
//    @ApiOperation("Update a storage item by given parameters. User validation for storage ownership.")
//    @PutMapping(path = "/update/item", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<StorageModel.Item> updateStorageItem(@RequestBody StorageModel.Item item)
//            throws ElementNotFoundException {
//        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        storageDB.validateUserForStorage(item.getStorageId(), user.getUsername());
//        return ResponseEntity.ok(new StorageItemModel(storageDB.updateStorageItem(item)));
//    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete a storage item via associated IDs. User validation for storage ownership.")
    @DeleteMapping(path = "/delete/item/{storageId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel.Item> deleteStorageItem(
            @ApiParam(value = "storage db id", example = "1") @PathVariable Integer storageId,
            @ApiParam(value = "product db id", example = "1") @PathVariable Integer productId)
            throws ElementNotFoundException {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        storageDB.validateUserForStorage(storageId, user.getUsername());
        return ResponseEntity.ok(new StorageModel.Item(storageDB.deleteStorageItem(storageId, productId)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete storage from the database by its generated ID. User validation for storage ownership - Admin exception.")
    @DeleteMapping(path = "/delete/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> deleteStorage(
            @ApiParam(value = "storage db id", example = "1") @PathVariable Integer storageId)
            throws ElementNotFoundException {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDB.getUserByName(user.getUsername()).hasRole(RoleName.ROLE_ADMIN)) {
            storageDB.validateUserForStorage(storageId, user.getUsername());
        }
        return ResponseEntity.ok(new StorageModel(storageDB.deleteStorageById(storageId)));
    }
}
