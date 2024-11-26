package com.example.serviceTopQuery.topManagement.api;

import com.example.serviceTopQuery.topManagement.model.Lending;
import org.mapstruct.Mapper;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public class LendingViewMapper {
    public LendingView toLendingView(Lending lending) {
        if (lending == null) {
            return null;
        }

        LendingView lendingView = new LendingView();

        lendingView.setId(lending.getId());
        lendingView.setLendingCode(lending.getLendingCode());
        lendingView.setReaderId(lending.getReaderId());
        lendingView.setBookId(lending.getBookId());
        lendingView.setBookTitle(lending.getBookTitle());
        lendingView.setLendDate(lending.getLendDate());
        lendingView.setLimitDate(lending.getLimitDate());
        lendingView.setReturnedDate(lending.getReturnedDate());
        lendingView.setDaysTillReturn(lending.getDaysTillReturn());
        lendingView.setDaysOverdue(lending.getDaysOverdue());
        lendingView.setFine(lending.getFine());
        lendingView.setReturned(lending.isReturned());
        lendingView.setComment(lending.getComment());

        return lendingView;
    }
    public Iterable<LendingView> toLendingView(Iterable<Lending> lendings) {
        if ( lendings == null ) {
            return null;
        }

        ArrayList<LendingView> iterable = new ArrayList<LendingView>();
        for ( Lending lending : lendings ) {
            iterable.add( toLendingView( lending ) );
        }

        return iterable;
    }
}
