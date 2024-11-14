package com.example.serviceAuth.userManagement.api;

import com.example.serviceAuth.userManagement.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-14T14:46:34+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class UserViewMapperImpl extends UserViewMapper {

    @Override
    public UserView toUserView(User user) {
        if ( user == null ) {
            return null;
        }

        UserView userView = new UserView();

        if ( user.getId() != null ) {
            userView.setId( String.valueOf( user.getId() ) );
        }
        userView.setUsername( user.getUsername() );
        userView.setFullName( user.getFullName() );

        return userView;
    }

    @Override
    public List<UserView> toUserView(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserView> list = new ArrayList<UserView>( users.size() );
        for ( User user : users ) {
            list.add( toUserView( user ) );
        }

        return list;
    }
}
