package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.location.Location;
import ru.practicum.ewm.models.location.LocationDto;

public class LocationMapper {

    public static LocationDto locationToDto(Location location) {
        return LocationDto.create(location.getLat(), location.getLon());
    }

}
