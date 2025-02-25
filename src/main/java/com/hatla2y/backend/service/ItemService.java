package com.hatla2y.backend.service;

import com.hatla2y.backend.dtos.Location;
import com.hatla2y.backend.dtos.item.ItemInfo;
import com.hatla2y.backend.exceptions.InvalidItemIdException;
import com.hatla2y.backend.exceptions.NotOwnedItemException;
import com.hatla2y.backend.models.Item;
import com.hatla2y.backend.models.ItemStatus;
import com.hatla2y.backend.models.User;
import com.hatla2y.backend.repositories.ItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public ItemService(ItemRepository itemRepository,
                       ImageService imageService,
                       ModelMapper modelMapper,
                       UserService userService) {
        this.itemRepository = itemRepository;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Transactional
    public ItemInfo createItem(ItemInfo item, User user) {
        Item newItem = modelMapper.map(item, Item.class);
        newItem.setCreatedBy(user);
        newItem = itemRepository.save(newItem);
        for (String url : item.getImagesURLs()) {
            imageService.create(url, newItem);
        }
        ItemInfo info = modelMapper.map(newItem, ItemInfo.class);
        info.setImagesURLs(item.getImagesURLs());
        return info;
    }

    private Item getItemById(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new InvalidItemIdException(id.toString()));
    }

    public ItemInfo getItemById(Integer id, User user) {
        Item item = getItemById(id);
        ItemInfo info = modelMapper.map(item, ItemInfo.class);
        info.setAuthor(item.getCreatedBy().getId().toString());
        item.getImages()
                .forEach(image -> {
                    info.getImagesURLs().add(image.getUrl());
                });
        return info;
    }

    @Transactional
    public ItemInfo changeItemStatus(Integer id, ItemStatus status, User user) {
        Item item = getItemById(id);
        item.setItemStatus(status);
        itemRepository.save(item);
        return modelMapper.map(item, ItemInfo.class);
    }

    @Transactional
    public void deleteItemById(Integer id, User user) {
        Item item = getItemById(id);
        if (item.getCreatedBy().getId().equals(user.getId())) {
            itemRepository.delete(item);
        }
        throw new NotOwnedItemException("user tried to delete an item that is not owned by the user");
    }

    public List<ItemInfo> getMyAllItems(User user) {
        return user.getItems().stream()
                .map(item -> {
                    return modelMapper.map(item, ItemInfo.class);
                })
                .toList();
    }

    public List<ItemInfo> getAllPublicItems(Integer userId) {
        User user = userService.getUserById(userId);
        return getMyAllItems(user);
    }

    public List<ItemInfo> getAllPublicItems(int size, int page) {
        Pageable request = PageRequest.of(page, size);
        return itemRepository.findAll(request)
                .stream()
                .map(item -> {
                    return modelMapper.map(item, ItemInfo.class);
                })
                .toList();
    }

    public List<ItemInfo> getAllPublicItemsInCategory(String category, ItemStatus status, int size, int page) {
        Pageable request = PageRequest.of(page, size);
        return itemRepository.findAll(request)
                .stream()
                .filter(item -> item.getCategory().equals(category))
                .filter(item -> item.getItemStatus() == status)
                .map(item -> {
                    return modelMapper.map(item, ItemInfo.class);
                })
                .toList();
    }

    public List<ItemInfo> getAllPublicItemsNearLocation(String location, ItemStatus status , int size, int page) {
        Pageable request = PageRequest.of(page, size);
        return itemRepository.findAll(request)
                .stream()
                .filter(item -> item.getItemStatus() == status)
                .filter(item -> item.getLocation().equals(location))
                .map(item -> {
                    return modelMapper.map(item, ItemInfo.class);
                })
                .toList();
    }

    public List<ItemInfo> getAllPublicItemsNearLocation(Location location, double distance, ItemStatus status,int size, int page) {
        PageRequest request = PageRequest.of(page, size);
        return itemRepository
                .findNearLocationCoords(location.getLatitude(), location.getLatitude(), distance, request)
                .stream()
                .filter(item -> item.getItemStatus() == status)
                .map(item -> {
                    return modelMapper.map(item, ItemInfo.class);
                })
                .toList();
    }

    public List<ItemInfo> getAllPublicItemWithFoundDateAfter(LocalDate date, ItemStatus status, int size, int page) {
        Pageable request = PageRequest.of(page, size);
        return itemRepository.findAllByFoundDateAfter(date, request)
                .stream()
                .filter(item -> item.getItemStatus() == status)
                .map(item -> {
                    return modelMapper.map(item, ItemInfo.class);
                })
                .toList();
    }
}
