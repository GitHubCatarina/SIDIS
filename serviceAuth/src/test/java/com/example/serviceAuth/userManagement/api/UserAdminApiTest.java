package com.example.serviceAuth.userManagement.api;

import com.example.serviceAuth.userManagement.model.User;
import com.example.serviceAuth.userManagement.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserAdminApiTest {

    @InjectMocks
    private UserAdminApi userAdminApi;

    @Mock
    private UserService userService;

    @Mock
    private UserViewMapper userViewMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldReturnUserView() {
        CreateUserRequest request = new CreateUserRequest();
        User user = new User();
        UserView userView = new UserView();

        when(userService.create(request)).thenReturn(user);
        when(userViewMapper.toUserView(user)).thenReturn(userView);

        UserView result = userAdminApi.create(request);

        assertNotNull(result);
        verify(userService, times(1)).create(request);
        verify(userViewMapper, times(1)).toUserView(user);
    }

    @Test
    void update_ShouldReturnUpdatedUserView() {
        Long userId = 1L;
        EditUserRequest request = new EditUserRequest();
        User user = new User();
        UserView userView = new UserView();

        when(userService.update(userId, request)).thenReturn(user);
        when(userViewMapper.toUserView(user)).thenReturn(userView);

        UserView result = userAdminApi.update(userId, request);

        assertNotNull(result);
        verify(userService, times(1)).update(userId, request);
        verify(userViewMapper, times(1)).toUserView(user);
    }

    @Test
    void delete_ShouldReturnDeletedUserView() {
        Long userId = 1L;
        User user = new User();
        UserView userView = new UserView();

        when(userService.delete(userId)).thenReturn(user);
        when(userViewMapper.toUserView(user)).thenReturn(userView);

        UserView result = userAdminApi.delete(userId);

        assertNotNull(result);
        verify(userService, times(1)).delete(userId);
        verify(userViewMapper, times(1)).toUserView(user);
    }

    @Test
    void get_ShouldReturnUserView() {
        Long userId = 1L;
        User user = new User();
        UserView userView = new UserView();

        when(userService.getUser(userId)).thenReturn(user);
        when(userViewMapper.toUserView(user)).thenReturn(userView);

        UserView result = userAdminApi.get(userId);

        assertNotNull(result);
        verify(userService, times(1)).getUser(userId);
        verify(userViewMapper, times(1)).toUserView(user);
    }

    @Test
    void search_ShouldReturnListResponseOfUserViews() {
        SearchRequest<SearchUsersQuery> request = new SearchRequest<>();
        User user = new User();
        List<User> userList = List.of(user);
        UserView userView = new UserView();

        when(userService.searchUsers(request.getPage(), request.getQuery())).thenReturn(userList);
        when(userViewMapper.toUserView(userList)).thenReturn(List.of(userView));

        ListResponse<UserView> result = userAdminApi.search(request);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        verify(userService, times(1)).searchUsers(request.getPage(), request.getQuery());
        verify(userViewMapper, times(1)).toUserView(userList);
    }
}
