package ru.practicum.model.location;

public class LocationMapper {

    public static Location mapToLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }

    public static LocationDto mapToLocationDto(Location location) {
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }


}
