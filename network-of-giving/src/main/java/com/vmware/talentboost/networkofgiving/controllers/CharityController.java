package com.vmware.talentboost.networkofgiving.controllers;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import com.vmware.talentboost.networkofgiving.services.ICharityService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "api/v1/charities", produces = MediaType.APPLICATION_JSON_VALUE)
public class CharityController {
    private final ICharityService charityService;

    public CharityController(ICharityService charityService) {
        this.charityService = charityService;
    }

    @GetMapping("/{title}")
    public Charity getCharity(@PathVariable("title") String title) {
        return charityService.getCharity(title);
    }

    @GetMapping
    public List<Charity> getAllCharities() {
        return charityService.getAllCharities();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addCharity(@RequestBody @Valid Charity charity) {
        charityService.addCharity(charity);
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