package com.vmware.talentboost.networkofgiving.controllers;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.services.user.IUserService;

import com.vmware.talentboost.networkofgiving.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable("username") String username) {
        return userService.getUser(username);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUser(@RequestBody @Valid User user) {
        userService.addUser(user);
    }

    @PutMapping(path = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable("username") String username, @RequestBody @Valid User user) {
        userService.updateUser(username, user);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
    }


    @GetMapping("/{username}/charities/participated")
    public List<Charity> getAllParticipatedCharity(@PathVariable("username") String username) {
        return userService.getAllParticipatedCharities(username);
    }

    @GetMapping("/{username}/charities/donated")
    public List<Charity> getAllDonatedCharities(@PathVariable("username") String username) {
        return userService.getAllDonatedCharities(username);
    }

    @GetMapping("/{username}/charities/created")
    public List<Charity> getAllCreatedCharities(@PathVariable("username") String username) {
        return userService.getAllCreatedCharities(username);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void userNotFoundExceptionHandler() {
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void incorrectSQLExceptionHandler() {
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void illegalArgumentsHandler() {
    }

}
