package com.hatla2y.backend.controllers;

import com.hatla2y.backend.dtos.item.ItemInfo;
import com.hatla2y.backend.models.Item;
import com.hatla2y.backend.models.ItemStatus;
import com.hatla2y.backend.models.User;
import com.hatla2y.backend.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
