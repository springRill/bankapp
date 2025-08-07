package com.blocker.controller;

import com.blocker.service.BlockerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/validate")
public class BlockerController {

    private final BlockerService blockerService;

    public BlockerController(BlockerService blockerService) {
        this.blockerService = blockerService;
    }

    @GetMapping("")
    public Boolean validate() throws AccountNotFoundException {
        return blockerService.validate();
    }
}
