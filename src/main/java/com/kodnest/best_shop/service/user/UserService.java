package com.kodnest.best_shop.service.user;

import com.kodnest.best_shop.exceptions.AlreadyExistException;
import com.kodnest.best_shop.exceptions.ResourceNotFoundException;
import com.kodnest.best_shop.model.User;
import com.kodnest.best_shop.repository.UserRepository;
import com.kodnest.best_shop.request.CreateUserRequest;
import com.kodnest.best_shop.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;


    @Override
    public User getUserId(Long userId) {
        return  userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request).filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setPassword(request.getPassword());
                    userRepository.save(user);
                    return user;
                }).orElseThrow(()->new AlreadyExistException("User already exists"));
    }

    @Override
 public User updateUser(UserUpdateRequest user, Long userId) {
     return userRepository.findById(userId).map(existingUser -> {
         existingUser.setFirstName(user.getFirstName());
         existingUser.setLastName(user.getLastName());
         return userRepository.save(existingUser);
     }).orElseThrow(() -> new ResourceNotFoundException("user not found"));
 }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, ()->{
            throw new ResourceNotFoundException("User not found");
        });
    }
}
