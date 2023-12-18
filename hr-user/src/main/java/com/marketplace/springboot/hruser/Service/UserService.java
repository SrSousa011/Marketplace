package com.marketplace.springboot.hruser.Service;

import com.marketplace.springboot.Model.UserModel;
import com.marketplace.springboot.hruser.Dto.UserRecordDto;
import com.marketplace.springboot.hruser.Exception.Impl.DeletedException;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserModel updateUser(UUID id, UserRecordDto productRecordDto) throws ChangeSetPersister.NotFoundException;

    UserModel saveUser(UserModel productModel);

    List<UserModel> getAllUsers() throws ChangeSetPersister.NotFoundException;

    Optional<UserModel> getUserById(UUID id) throws ChangeSetPersister.NotFoundException;

    void deleteUser(UUID id) throws DeletedException, ChangeSetPersister.NotFoundException;
}
