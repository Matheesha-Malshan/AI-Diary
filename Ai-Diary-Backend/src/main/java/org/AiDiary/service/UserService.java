package org.AiDiary.service;

import org.AiDiary.dto.request.UserRequestDto;

public interface UserService {
    UserRequestDto createUser(UserRequestDto userRequestDto);
}
