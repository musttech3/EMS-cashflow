package com.alphasoft.EMS.rest;


import com.alphasoft.EMS.dto.UserResponse;
import com.alphasoft.EMS.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@AllArgsConstructor
public class DemoController {

    @GetMapping
    public ResponseEntity<String> sayHello(
            HttpServletRequest httpRequest,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt
    ) {
        return ResponseEntity.ok("Hello world, Success");
    }

}
