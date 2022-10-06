package com.freshplanner.api.controller;

import com.freshplanner.api.controller.model.Storage;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.NoAccessException;
import com.freshplanner.api.security.SecurityContext;
import com.freshplanner.api.service.storage.StorageDB;
import com.freshplanner.api.service.storage.StorageEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageDB storageDB;

    @Autowired
    public StorageController(StorageDB storageDB) {
        this.storageDB = storageDB;
    }

    // === POST ========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Insert a new storage for the user.")
    @PostMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Storage> addStorage(@RequestBody Storage storageModel) throws ElementNotFoundException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(storageDB.insertStorage(username, storageModel).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Insert a new storage item into the database. UserEntity validation for storage ownership.")
    @PostMapping(path = "/insert-item/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Storage> addStorageItem(@ApiParam(value = "storage db id", example = "1")
                                                  @PathVariable Integer storageId,
                                                  @RequestBody Storage.Item item) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(storageDB.insertStorageItem(username, storageId, item).mapToModel());
    }

    // === GET =========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Get all storages from the user.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Storage>> getUserStorages() {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(
                storageDB.selectUserStorages(username)
                        .stream().map(StorageEntity::mapToModel).collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Get storage by database ID. UserEntity validation for storage ownership.")
    @GetMapping(path = "/get/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Storage> getStorageById(@ApiParam(value = "storage db id", example = "1")
                                                  @PathVariable Integer storageId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(storageDB.selectStorageById(username, storageId).mapToModel());
    }

    // === PUT =========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update storage with additional user. UserEntity validation for storage ownership.")
    @PutMapping(path = "/add-user/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Storage> addUser(@ApiParam(value = "storage db id", example = "1")
                                           @PathVariable Integer storageId,
                                           @ApiParam(value = "username", example = "Max")
                                           @RequestParam String username) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(storageDB.updateAddUser(usernameOwner, storageId, username).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update storage by removing user. UserEntity validation for storage ownership.")
    @PutMapping(path = "/remove-user/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Storage> removeUser(@ApiParam(value = "storage db id", example = "1")
                                              @PathVariable Integer storageId,
                                              @ApiParam(value = "username", example = "Max")
                                              @RequestParam String username) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(storageDB.updateRemoveUser(usernameOwner, storageId, username).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update storage in the database. UserEntity validation for storage ownership.")
    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Storage> updateStorage(@RequestBody Storage storageModel) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(storageDB.updateStorage(usernameOwner, storageModel).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update storage item in the database. UserEntity validation for storage ownership.")
    @PutMapping(path = "/update-item/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Storage.Item> updateStorageItem(@ApiParam(value = "storage db id", example = "1")
                                                          @PathVariable Integer storageId,
                                                          @RequestBody Storage.Item itemModel) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(storageDB.updateStorageItem(username, storageId, itemModel).mapToModel());
    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete storage item from the database by IDs. UserEntity validation for storage ownership.")
    @DeleteMapping(path = "/delete-item/{storageId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Storage> deleteStorageItem(@ApiParam(value = "storage db id", example = "1")
                                                     @PathVariable Integer storageId,
                                                     @ApiParam(value = "product db id", example = "1")
                                                     @PathVariable Integer productId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(storageDB.deleteStorageItem(username, storageId, productId).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete storage from the database by ID. UserEntity validation for storage ownership - Admin exception.")
    @DeleteMapping(path = "/delete/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Storage> deleteStorage(@ApiParam(value = "storage db id", example = "1")
                                                 @PathVariable Integer storageId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(storageDB.deleteStorageById(username, storageId).mapToModel());
    }
}
