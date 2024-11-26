package com.example.serviceLendingQuery.lendingManagement.api;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
@Component
public class LendingAvgPerGenrePerMonthViewMapper {

    public LendingAvgPerGenrePerMonthView toLendingAvgPerGenrePerMonthView(Object[] result) {
        if (result == null) {
            return null;
        }

        Long genreId = (Long) result[0];
        Double averageDuration = (Double) result[1];

        return new LendingAvgPerGenrePerMonthView(genreId, averageDuration);
    }

    public Iterable<LendingAvgPerGenrePerMonthView> toLendingAvgPerGenrePerMonthViewList(List<Object[]> results) {
        if (results == null) {
            return null;
        }

        List<LendingAvgPerGenrePerMonthView> views = new ArrayList<>();
        for (Object[] result : results) {
            views.add(toLendingAvgPerGenrePerMonthView(result));
        }

        return views;
    }
}
