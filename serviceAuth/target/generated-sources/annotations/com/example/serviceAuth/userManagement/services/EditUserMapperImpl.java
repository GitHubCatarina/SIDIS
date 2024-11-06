package com.example.serviceAuth.userManagement.services;

import com.example.serviceAuth.userManagement.model.Role;
import com.example.serviceAuth.userManagement.model.User;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-06T11:58:26+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class EditUserMapperImpl extends EditUserMapper {

    @Override
    public User create(CreateUserRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( request.getUsername() );
        if ( user.getAuthorities() != null ) {
            Set<Role> set = stringToRole( request.getAuthorities() );
            if ( set != null ) {
                user.getAuthorities().addAll( set );
            }
        }
        user.setPassword( request.getPassword() );
        user.setFullName( request.getFullName() );

        logAfterMapping( user );

        return user;
    }

    @Override
    public void update(EditUserRequest request, User user) {
        if ( request == null ) {
            return;
        }

        if ( user.getAuthorities() != null ) {
            user.getAuthorities().clear();
            Set<Role> set = stringToRole( request.getAuthorities() );
            if ( set != null ) {
                user.getAuthorities().addAll( set );
            }
        }
        if ( request.getFullName() != null ) {
            user.setFullName( request.getFullName() );
        }

        logAfterMapping( user );
    }
}
