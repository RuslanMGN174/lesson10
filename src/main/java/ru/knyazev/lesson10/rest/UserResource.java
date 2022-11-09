package ru.knyazev.lesson10.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.knyazev.lesson10.controller.BadRequestException;
import ru.knyazev.lesson10.controller.NotFoundException;
import ru.knyazev.lesson10.service.UserDTO;
import ru.knyazev.lesson10.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/all", produces = "application/json")
    public List<UserDTO> findAll() {
        return userService.findAll().stream()
                .peek(u -> u.setPassword(null))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public UserDTO findById(@PathVariable("id") Long id) {
        UserDTO userDTO = userService.findById(id)
                .orElseThrow(NotFoundException::new);
        userDTO.setPassword(null);
        return userDTO;
    }

    @GetMapping("filter")
    public List<UserDTO> listPage(
                           @RequestParam("usernameFilter") Optional<String> usernameFilter,
                           @RequestParam("ageMinFilter") Optional<Integer> ageMinFilter,
                           @RequestParam("ageMaxFilter") Optional<Integer> ageMaxFilter,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam("sortField") Optional<String> sortField) {

        return userService.findWithFilter(
                usernameFilter.orElse(null),
                ageMinFilter.orElse(null),
                ageMaxFilter.orElse(null),
                size.orElse(3),
                sortField.orElse(null)
        );
    }

    @Secured("SUPER_ADMIN")
    @PostMapping(consumes = "application/json")
    public UserDTO create(@RequestBody UserDTO userDTO) {
        if (userDTO.getId() != null) {
            throw new BadRequestException();
        }
        userService.save(userDTO);
        return userDTO;
    }

    @Secured("SUPER_ADMIN")
    @PutMapping(consumes = "application/json")
    public void update(@RequestBody UserDTO userDTO) {
        if (userDTO.getId() == null) {
            throw new BadRequestException();
        }
        userService.save(userDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @ExceptionHandler
    public ResponseEntity<String> notFoundException(NotFoundException ex) {
        return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> badRequestException(BadRequestException ex) {
        return new ResponseEntity<>("Bad request", HttpStatus.NOT_FOUND);
    }
}
