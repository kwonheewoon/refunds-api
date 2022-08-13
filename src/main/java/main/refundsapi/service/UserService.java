package main.refundsapi.service;

import lombok.RequiredArgsConstructor;
import main.refundsapi.dto.UserApiDto;
import main.refundsapi.dto.UserDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.repository.UserQueryRepository;
import main.refundsapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private UserQueryRepository userQueryRepository;

    public UserApiDto saveUser(UserDto userDto){

        return UserApiDto.UserApiDtoConvert(userRepository.save(
                UserEntity.builder()
                .name(userDto.getName())
                .userId(userDto.getUserId())
                .password(userDto.getPassword())
                .regNo(userDto.getReqNo())
                .build()
        ));
    }
}
