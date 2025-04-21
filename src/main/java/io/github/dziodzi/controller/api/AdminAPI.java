package io.github.dziodzi.controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin", description = "Admin management API")
@RequestMapping("/admin")
public interface AdminAPI {

    @GetMapping("/health")
    String index();
}