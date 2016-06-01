/*Copyright (c) 2016-2017 prefab.com All Rights Reserved.
 This software is the confidential and proprietary information of prefab.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with prefab.com*/
package com.hrdb.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.file.model.Downloadable;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.hrdb.User;
import com.hrdb.service.UserService;
import com.wordnik.swagger.annotations.*;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;

/**
 * Controller object for domain model class User.
 * @see User
 */
@RestController("hrdb.UserController")
@RequestMapping("/hrdb/User")
@Api(description = "Exposes APIs to work with User resource.", value = "UserController")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    @Qualifier("hrdb.UserService")
    private UserService userService;

    /**
     * @deprecated Use {@link #findUsers(String)} instead.
     */
    @Deprecated
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation(value = "Returns the list of User instances matching the search criteria.")
    public Page<User> findUsers(Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering Users list");
        return userService.findAll(queryFilters, pageable);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Returns the list of User instances matching the search criteria.")
    public Page<User> findUsers(@RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Users list");
        return userService.findAll(query, pageable);
    }

    @RequestMapping(value = "/export/{exportType}", method = RequestMethod.GET, produces = "application/octet-stream")
    @ApiOperation(value = "Returns downloadable file for the data.")
    public Downloadable exportUsers(@PathVariable("exportType") ExportType exportType, @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        return userService.export(exportType, query, pageable);
    }

    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service UserService instance
	 */
    protected void setUserService(UserService service) {
        this.userService = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "Creates a new User instance.")
    public User createUser(@RequestBody User user) {
        LOGGER.debug("Create User with information: {}", user);
        user = userService.create(user);
        LOGGER.debug("Created User with information: {}", user);
        return user;
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "Returns the total count of User instances.")
    public Long countUsers(@RequestParam(value = "q", required = false) String query) {
        LOGGER.debug("counting Users");
        return userService.count(query);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "Returns the User instance associated with the given id.")
    public User getUser(@PathVariable(value = "id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Getting User with id: {}", id);
        User foundUser = userService.getById(id);
        LOGGER.debug("User details with id: {}", foundUser);
        return foundUser;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "Updates the User instance associated with the given id.")
    public User editUser(@PathVariable(value = "id") Integer id, @RequestBody User user) throws EntityNotFoundException {
        LOGGER.debug("Editing User with id: {}", user.getUserid());
        user.setUserid(id);
        user = userService.update(user);
        LOGGER.debug("User details with id: {}", user);
        return user;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "Deletes the User instance associated with the given id.")
    public boolean deleteUser(@PathVariable(value = "id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Deleting User with id: {}", id);
        User deletedUser = userService.delete(id);
        return deletedUser != null;
    }
}
