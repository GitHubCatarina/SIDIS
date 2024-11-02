package com.example.serviceAuth.userManagement.repositories;

import com.example.serviceAuth.userManagement.model.User;
import com.example.serviceAuth.userManagement.services.Page;
import com.example.serviceAuth.userManagement.services.SearchUsersQuery;

import java.util.List;

interface UserRepoCustom {
    List<User> searchUsers(Page page, SearchUsersQuery query);
}
