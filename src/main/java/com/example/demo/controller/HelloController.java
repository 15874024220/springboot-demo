package com.example.demo.controller;

import com.example.demo.entity.Message;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public Map<String, Object> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello Spring Boot!");
        response.put("timestamp", LocalDateTime.now());
        response.put("javaVersion", System.getProperty("java.version"));
        return response;
    }

    @GetMapping(path = "/{id}")
    public Map<String, Object> hello(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("message", "Hello Spring Boot!");
        response.put("timestamp", LocalDateTime.now());
        response.put("javaVersion", System.getProperty("java.version"));
        return response;
    }

    @GetMapping("/error")
    public Map<String, Object> error() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "error test!");
        response.put("timestamp", LocalDateTime.now());
        response.put("javaVersion", System.getProperty("java.version"));
        return response;
    }

    @PostMapping("/post")
    public Message post(@RequestBody Message map) {
        return map;
    }
}