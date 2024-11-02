package com.example.serviceLending.lendingManagement.services;

import com.example.serviceLending.lendingManagement.model.Lending;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-02T16:14:00+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class EditLendingMapperImpl extends EditLendingMapper {

    @Override
    public Lending create(CreateLendingRequest request) {
        if ( request == null ) {
            return null;
        }

        Lending lending = new Lending();

        lending.setReaderId( request.getReaderId() );
        lending.setBookId( request.getBookId() );

        return lending;
    }
}
