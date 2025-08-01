package com.account.mapper;

import com.account.domain.User;
import com.account.dto.UserDto;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setPersonName(user.getPersonName());
        userDto.setDateOfBirth(user.getDateOfBirth());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setPersonName(userDto.getPersonName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        return user;
    }

}
