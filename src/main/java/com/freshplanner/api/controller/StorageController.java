package com.freshplanner.api.controller;

import com.freshplanner.api.database.storage.StorageDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.NoAccessException;
import com.freshplanner.api.model.storage.StorageModel;
import com.freshplanner.api.model.storage.StorageSummaryModel;
import com.freshplanner.api.security.SecurityContext;
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
    public ResponseEntity<StorageModel> addStorage(@RequestBody StorageSummaryModel storageModel) throws ElementNotFoundException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new StorageModel(
                storageDB.insertStorage(username, storageModel)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Insert a new storage item into the database. User validation for storage ownership.")
    @PostMapping(path = "/insert-item/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> addStorageItem(@ApiParam(value = "storage db id", example = "1")
                                                       @PathVariable Integer storageId,
                                                       @RequestBody StorageModel.Item item) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new StorageModel(
                storageDB.insertStorageItem(username, storageId, item)));
    }

    // === GET =========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Get all storages from the user.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StorageSummaryModel>> getUserStorages() {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(
                storageDB.selectUserStorages(username)
                        .stream().map(StorageSummaryModel::new).collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Get storage by database ID. User validation for storage ownership.")
    @GetMapping(path = "/get/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> getStorageById(@ApiParam(value = "storage db id", example = "1")
                                                       @PathVariable Integer storageId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new StorageModel(
                storageDB.selectStorageById(username, storageId)));
    }

    // === PUT =========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update storage with additional user. User validation for storage ownership.")
    @PutMapping(path = "/add-user/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> addUser(@ApiParam(value = "storage db id", example = "1")
                                                @PathVariable Integer storageId,
                                                @ApiParam(value = "username", example = "Max")
                                                @RequestParam String username) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(new StorageModel(
                storageDB.updateAddUser(usernameOwner, storageId, username)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update storage by removing user. User validation for storage ownership.")
    @PutMapping(path = "/remove-user/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> removeUser(@ApiParam(value = "storage db id", example = "1")
                                                   @PathVariable Integer storageId,
                                                   @ApiParam(value = "username", example = "Max")
                                                   @RequestParam String username) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(new StorageModel(
                storageDB.updateRemoveUser(usernameOwner, storageId, username)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update storage in the database. User validation for storage ownership.")
    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> updateStorage(@RequestBody StorageModel storageModel) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(new StorageModel(
                storageDB.updateStorage(usernameOwner, storageModel)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update storage item in the database. User validation for storage ownership.")
    @PutMapping(path = "/update-item/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel.Item> updateStorageItem(@ApiParam(value = "storage db id", example = "1")
                                                               @PathVariable Integer storageId,
                                                               @RequestBody StorageModel.Item itemModel) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new StorageModel.Item(
                storageDB.updateStorageItem(username, storageId, itemModel)));
    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete storage item from the database by IDs. User validation for storage ownership.")
    @DeleteMapping(path = "/delete-item/{storageId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> deleteStorageItem(@ApiParam(value = "storage db id", example = "1")
                                                          @PathVariable Integer storageId,
                                                          @ApiParam(value = "product db id", example = "1")
                                                          @PathVariable Integer productId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new StorageModel(
                storageDB.deleteStorageItem(username, storageId, productId)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete storage from the database by ID. User validation for storage ownership - Admin exception.")
    @DeleteMapping(path = "/delete/{storageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageModel> deleteStorage(@ApiParam(value = "storage db id", example = "1")
                                                      @PathVariable Integer storageId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new StorageModel(
                storageDB.deleteStorageById(username, storageId)));
    }
}
