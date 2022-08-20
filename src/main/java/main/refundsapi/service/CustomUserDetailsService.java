package main.refundsapi.service;

import lombok.RequiredArgsConstructor;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        var result = userRepository.findByUserId(userId);

        return userRepository.findByUserId(userId)
                .map(this::createUser)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
    }

    private User createUser(UserEntity userEntity) {
        if (null == userEntity) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        return new User(userEntity.getUserId(),
                userEntity.getPassword(),
                Arrays.asList());
    }
}
