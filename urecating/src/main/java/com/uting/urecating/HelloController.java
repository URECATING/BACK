package com.uting.urecating;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloController {
    @GetMapping("/")
    @ResponseBody
    public String hi() {
        return "UrecaTing - SOS..Taeyeon!!!";
    }

    @GetMapping("/springfoxtest")
    public ResponseEntity<Object> authHeaderChecker(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>(){{
            put("Authorization", request.getHeader("Authorization"));
        }};
        return ResponseEntity.ok(response);
    }
}
