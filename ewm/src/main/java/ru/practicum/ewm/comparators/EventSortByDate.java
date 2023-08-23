package ru.practicum.ewm.comparators;

import ru.practicum.ewm.models.event.EventShortDto;

import java.util.Comparator;

public class EventSortByDate implements Comparator<EventShortDto> {
    @Override
    public int compare(EventShortDto o1, EventShortDto o2) {
        if (o1.getEventDate().isBefore(o2.getEventDate())) {
            return -1;
        } else if (o1.getEventDate().isAfter(o2.getEventDate())) {
            return 1;
        } else return 0;
    }
}
