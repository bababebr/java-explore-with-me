package ru.practicum.ewm.comparators;

import ru.practicum.ewm.models.event.EventShortDto;

import java.util.Comparator;

public class EventSortByViews implements Comparator<EventShortDto> {
    @Override
    public int compare(EventShortDto o1, EventShortDto o2) {
        if (o1.getViews() < o2.getViews()) {
            return -1;
        } else if (o1.getViews() > o2.getViews()) {
            return 1;
        } else return 0;
    }
}
