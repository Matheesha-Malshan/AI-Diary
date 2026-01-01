package org.AiDiary.controller;

import lombok.RequiredArgsConstructor;
import org.AiDiary.dto.request.UserRequestDto;
import org.AiDiary.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public UserRequestDto createuser(@RequestBody UserRequestDto userRequestDto){
        return userService.createUser(userRequestDto);
    }

}
