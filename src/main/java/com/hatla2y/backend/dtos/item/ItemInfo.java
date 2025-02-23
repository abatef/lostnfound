package com.hatla2y.backend.dtos.item;

import com.hatla2y.backend.dtos.Location;
import com.hatla2y.backend.models.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfo {
    private Integer id;
    private String title;
    private String description;
    private String location;
    private Location locationCoordinates;
    private String category;
    private List<String> imagesURLs = new ArrayList<>();
    private String author;
    private ItemStatus status;
    private LocalDate lostDate;
    private LocalDate foundDate;
    private Double range;
    private Instant createdAt;
    private Instant updatedAt;
}
