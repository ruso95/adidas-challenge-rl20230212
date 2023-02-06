package com.adidas.backend.publicservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dummy")
public class DummyRestController {

  private static final String DUMMY_RESPONSE = "Hello, this is a dummy response from public service";

  @GetMapping
  public ResponseEntity<String> getDummyEndpointResponse() {
    return ResponseEntity
        .ok()
        .body(DUMMY_RESPONSE);
  }

}
