package com.hatla2y.backend.repositories;

import com.hatla2y.backend.models.Item;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query(value = "SELECT i from Item i where ST_Within(i.locationCoords, ST_SetSRID(ST_MakePoint(:lat, :lng), 4326), :dist)")
    Page<Item> findNearLocationCoords(@Param("lat") double lat,
                                      @Param("lng") double lng,
                                      @Param("dist") double distance, Pageable pageable);

    Page<Item> findAllByFoundDateAfter(LocalDate foundDateAfter, Pageable pageable);
}
