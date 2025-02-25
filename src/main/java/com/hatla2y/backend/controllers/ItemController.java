package com.hatla2y.backend.controllers;

import com.hatla2y.backend.dtos.Location;
import com.hatla2y.backend.dtos.item.ItemInfo;
import com.hatla2y.backend.models.Item;
import com.hatla2y.backend.models.ItemStatus;
import com.hatla2y.backend.models.User;
import com.hatla2y.backend.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemInfo> postItem(@RequestBody ItemInfo itemInfo, @AuthenticationPrincipal User user) {
        ItemInfo item = itemService.createItem(itemInfo, user);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ItemInfo> getItem(@RequestParam("id") Integer id, @AuthenticationPrincipal User user) {
        ItemInfo info = itemService.getItemById(id, user);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ItemInfo> updateItemStatus(@PathVariable("id") Integer id,
                                                     @RequestParam("status")ItemStatus status,
                                                     @AuthenticationPrincipal User user) {
        ItemInfo info = itemService.changeItemStatus(id, status, user);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Integer id, @AuthenticationPrincipal User user) {
        itemService.deleteItemById(id, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemInfo>> getAllPublicItems(@RequestParam("size") int size,
                                                            @RequestParam("page") int page) {
        List<ItemInfo> items = itemService.getAllPublicItems(size, page);
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/all/location")
    public ResponseEntity<List<ItemInfo>> getAllPublicItemsNearLocation(@RequestParam("location") String location,
                                                                        @RequestParam("status") ItemStatus status,
                                                                        @RequestParam("size") int size,
                                                                        @RequestParam("page") int page) {
        List<ItemInfo> items = itemService.getAllPublicItemsNearLocation(location, status, size, page);
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/all/nearby")
    public ResponseEntity<List<ItemInfo>> getAllPublicItemsNearCoords(@RequestParam("lat") double lat,
                                                                      @RequestParam("lng") double lng,
                                                                      @RequestParam("distance") double distance,
                                                                      @RequestParam("status") ItemStatus status,
                                                                      @RequestParam("size") int size,
                                                                      @RequestParam("page") int page) {
        List<ItemInfo> items = itemService.getAllPublicItemsNearLocation(new Location(lat, lng), distance, status, size, page);
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/all/category")
    public ResponseEntity<List<ItemInfo>> getAllItemsInCategory(@RequestParam("cat") String category,
                                                                @RequestParam(value = "status", required = false) ItemStatus status,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("page") int page) {
        List<ItemInfo> items = itemService.getAllPublicItemsInCategory(category, status, size, page);
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/all/found-after")
    public ResponseEntity<List<ItemInfo>> getAllPublicItemsWithFoundDateAfter(@RequestParam("date") LocalDate date,
                                                                              @RequestParam("status") ItemStatus status,
                                                                              @RequestParam("size") int size,
                                                                              @RequestParam("page") int page) {
        List<ItemInfo> items = itemService.getAllPublicItemWithFoundDateAfter(date, status, size, page);
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
