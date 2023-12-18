package com.marketplace.springboot.hruser.Service.Impl;

import com.marketplace.springboot.Model.UserModel;
import com.marketplace.springboot.hruser.Dto.UserRecordDto;
import com.marketplace.springboot.hruser.Exception.Impl.DeletedException;
import com.marketplace.springboot.hruser.Exception.Impl.DuplicatedException;
import com.marketplace.springboot.hruser.Exception.Impl.NotFoundException;
import com.marketplace.springboot.hruser.Repository.UserRepository;
import com.marketplace.springboot.hruser.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserModel updateUser(UUID id, UserRecordDto userRecordDto) throws NotFoundException {
        Optional<UserModel> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            UserModel existingUserModel = existingUser.get();
            BeanUtils.copyProperties(userRecordDto, existingUserModel, getNullPropertyNames(userRecordDto));

            return userRepository.save(existingUserModel);
        } else {
            throw new NotFoundException("User with ID " + id);
        }
    }

    @Transactional
    public UserModel saveUser(UserModel userModel) {
        if (isAlreadyExisting(userModel)) {
            throw new DuplicatedException("Product with name '" + userModel.getUsername());
        }
        return userRepository.save(userModel);
    }

    private boolean isAlreadyExisting(UserModel userModel) {
        return userRepository.existsByUsername(userModel.getUsername());
    }


    @Transactional
    public List<UserModel> getAllUsers() throws NotFoundException {
        List<UserModel> usersList = userRepository.findAll();
        if (usersList.isEmpty()) {
            throw new NotFoundException("No users have been found");
        }
        return usersList;
    }

    @Transactional
    public Optional<UserModel> getUserById(UUID id) throws NotFoundException {
        Optional<UserModel> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("User with ID " + id);
        }

        return userOptional;
    }

    @Transactional
    public void deleteUser(UUID id) throws DeletedException, NotFoundException {
        Optional<UserModel> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User with ID " + id);
        }

        try {
            userRepository.delete(userOptional.get());
            throw new DeletedException("User with ID " + id + " has been deleted.");
        } catch (Exception e) {
            throw new DeletedException("Failed to delete user with ID " + id + ".");
        }
    }

    private String[] getNullPropertyNames(UserRecordDto userRecordDto) {
        BeanWrapper srcBeanWrapper = new BeanWrapperImpl(userRecordDto);
        PropertyDescriptor[] propertyDescriptors = srcBeanWrapper.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            Object srcValue = srcBeanWrapper.getPropertyValue(propertyName);
            if (srcValue == null) emptyNames.add(propertyName);
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
