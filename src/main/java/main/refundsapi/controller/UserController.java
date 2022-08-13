package main.refundsapi.controller;

import lombok.RequiredArgsConstructor;
import main.refundsapi.dto.UserDto;
import main.refundsapi.service.UserService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/szs")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UserDto userDto){
        JSONObject results = new JSONObject();

        results.put("data", userService.saveUser(userDto));

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

}
