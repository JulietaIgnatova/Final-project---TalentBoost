package com.vmware.talentboost.networkofgiving.controllers;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import com.vmware.talentboost.networkofgiving.models.UserAction;
import com.vmware.talentboost.networkofgiving.services.user.IUserService;
import com.vmware.talentboost.networkofgiving.services.useraction.IUserActionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "api/v1/actions", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserActionController {

    private final IUserActionService userActionService;

    public UserActionController(IUserActionService userActionService) {
        this.userActionService = userActionService;
    }

    @GetMapping
    public List<UserAction> getLatestActivities(@RequestParam(name = "limit",defaultValue = "20",required = false) int limit) {
        return userActionService.getLatestActivities(limit);
    }

    @GetMapping("/{id}")
    public List<UserAction>  getLatestActivitiesForUser(@PathVariable("id") int userId,@RequestParam(name = "limit",defaultValue = "20",required = false) int limit) {
        return userActionService.getLatestActivities(limit);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserAction(@RequestBody @Valid UserAction userAction) {
        userActionService.addUserAction(userAction);
    }

}
