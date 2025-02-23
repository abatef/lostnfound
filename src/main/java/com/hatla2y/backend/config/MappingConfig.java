package com.hatla2y.backend.config;

import com.hatla2y.backend.dtos.Location;
import com.hatla2y.backend.dtos.item.ItemInfo;
import com.hatla2y.backend.models.Item;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfig {

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        ModelMapper rawModelMapper = new ModelMapper();
        Converter<Item, ItemInfo> itemToItemInfoConverter = ctx -> {
            ItemInfo itemInfo = rawModelMapper.map(ctx.getSource(), ItemInfo.class);
            itemInfo.setAuthor(ctx.getSource().getCreatedBy().getFullName());
            ctx.getSource().getImages().forEach(image -> {
                itemInfo.getImagesURLs().add(image.getUrl());
            });
            return itemInfo;
        };
        Converter<Location, Point> locationPointConverter = ctx -> {
            Location location = ctx.getSource();
            return geometryFactory.createPoint(new Coordinate(location.getLatitude(), location.getLongitude()));
        };

        Converter<Point, Location> pointLocationConverter = ctx -> {
            Point point = ctx.getSource();
            return Location.of(point);
        };
        modelMapper.addConverter(itemToItemInfoConverter);
        modelMapper.addConverter(locationPointConverter);
        modelMapper.addConverter(pointLocationConverter);
        return modelMapper;
    }
}
