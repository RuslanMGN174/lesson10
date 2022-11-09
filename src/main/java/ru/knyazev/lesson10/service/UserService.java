package ru.knyazev.lesson10.service;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> findAll();

    List<UserDTO> findWithFilter(String usernameFilter, Integer minAge, Integer maxAge,
                                  Integer size, String sortField);

    Optional<UserDTO> findById(long id);

    void save(UserDTO user);

    void delete(long id);
}
