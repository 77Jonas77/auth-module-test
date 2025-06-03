package dev.exampleinz.auth_module.infrastructure.adapter.input.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/oauth")
public class TestOAuthController {

    @GetMapping("/all")
    public String allAccess() {
        return "hello there!.";
    }

}
