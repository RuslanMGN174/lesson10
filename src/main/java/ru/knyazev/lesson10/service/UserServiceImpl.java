package ru.knyazev.lesson10.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knyazev.lesson10.persist.User;
import ru.knyazev.lesson10.persist.UserRepository;
import ru.knyazev.lesson10.persist.UserSpecification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findWithFilter(String usernameFilter, Integer minAge, Integer maxAge,
                                        /*Integer page,*/ Integer size, String sortField) {
        Specification<User> spec = Specification.where(null);
        if (usernameFilter != null && !usernameFilter.isBlank()) {
            spec = spec.and(UserSpecification.usernameLike(usernameFilter));
        }
        if (minAge != null) {
            spec = spec.and(UserSpecification.minAge(minAge));
        }
        if (maxAge != null) {
            spec = spec.and(UserSpecification.maxAge(maxAge));
        }
        if (sortField != null && !sortField.isBlank()) {
            return userRepository.findAll(spec).stream()
                    .map(UserDTO::new)
                    .collect(Collectors.toList());
        }
        return userRepository.findAll(spec).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Optional<UserDTO> findById(long id) {
       return userRepository.findById(id)
                .map(UserDTO::new);
    }

    @Transactional
    @Override
    public void save(UserDTO user) {
        User userToSave = new User(user);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        userRepository.save(userToSave);
        if (user.getId() == null) {
            user.setId(userToSave.getId());
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
