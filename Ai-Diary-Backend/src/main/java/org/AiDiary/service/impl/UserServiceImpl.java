package org.AiDiary.service.impl;

import lombok.RequiredArgsConstructor;
import org.AiDiary.dto.request.UserRequestDto;
import org.AiDiary.model.UserEntity;
import org.AiDiary.repository.UserRepository;
import org.AiDiary.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final ModelMapper mapper;

    @Override
    public UserRequestDto createUser(UserRequestDto userRequestDto){

        var user= userRepository.save(mapper.map(userRequestDto, UserEntity.class));
        return mapper.map(user,UserRequestDto.class);
    }

}
