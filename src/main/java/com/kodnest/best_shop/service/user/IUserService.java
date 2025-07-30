package com.kodnest.best_shop.service.user;

import com.kodnest.best_shop.model.User;
import com.kodnest.best_shop.request.CreateUserRequest;
import com.kodnest.best_shop.request.UserUpdateRequest;

public interface IUserService {
    User getUserId(Long userId);
    User createUser(CreateUserRequest user);
    User updateUser(UserUpdateRequest user,Long userId);
    void deleteUser(Long userId);
}
