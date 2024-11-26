package com.example.serviceLendingQuery.lendingManagement.api;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
@Component
public class LendingAvgPerBookViewMapper {

    public LendingAvgPerBookView toLendingAvgPerBookView(Object[] result) {
        if (result == null) {
            return null;
        }

        Long bookId = (Long) result[0];
        Double averageDuration = (Double) result[1];

        return new LendingAvgPerBookView(bookId, averageDuration);
    }

    public Iterable<LendingAvgPerBookView> toLendingAvgPerBookViewList(List<Object[]> results) {
        if (results == null) {
            return null;
        }

        List<LendingAvgPerBookView> views = new ArrayList<>();
        for (Object[] result : results) {
            views.add(toLendingAvgPerBookView(result));
        }

        return views;
    }
}
