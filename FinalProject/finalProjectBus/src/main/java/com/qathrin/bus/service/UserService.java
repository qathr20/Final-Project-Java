package com.qathrin.bus.service;

import com.qathrin.bus.dto.mapper.UserMapper;
import com.qathrin.bus.dto.model.user.UserDto;
import com.qathrin.bus.exception.BRSException;
import com.qathrin.bus.exception.EntityType;
import com.qathrin.bus.exception.ExceptionType;
import com.qathrin.bus.model.ERole;
import com.qathrin.bus.model.Role;
import com.qathrin.bus.model.User;
import com.qathrin.bus.repository.RoleRepository;
import com.qathrin.bus.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.qathrin.bus.exception.EntityType.USER;
import static com.qathrin.bus.exception.ExceptionType.DUPLICATE_ENTITY;
import static com.qathrin.bus.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusReservationService busReservationService;

    @Autowired
    private ModelMapper modelMapper;

    public List<User> ListAllUser() {
        return userRepository.findAll();
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(Long.valueOf(id));
    }

    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    public UserDto signup(UserDto userDto) {
        Role userRole;
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            if (userDto.isAdmin()) {
                userRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
            } else {
                userRole = roleRepository.findByName(ERole.ROLE_USER).get();
            }
            user = new User();
            user.setEmail(userDto.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            user.setRoles(new HashSet<>(Arrays.asList(userRole)));
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setMobileNumber(userDto.getMobileNumber());
            ;
            return UserMapper.toUserDto(userRepository.save(user));
        }
        throw exception(USER, DUPLICATE_ENTITY, userDto.getEmail());
    }

    public UserDto findUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return modelMapper.map(user, UserDto.class);
        }
        throw exception(USER, ENTITY_NOT_FOUND, email);
    }

    public UserDto findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return UserMapper.toUserDto(user.get());
        }
        throw exception(USER, ENTITY_NOT_FOUND, username);
    }

    /**
     * Update User Profile
     *
     * @param userDto
     * @return
     */

    public UserDto updateProfile(UserDto userDto) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if (user.isPresent()) {
            User userModel = user.get();
            userModel.setFirstName(userDto.getFirstName());
            userModel.setLastName(userDto.getLastName());
            userModel.setMobileNumber(userDto.getMobileNumber());
            return UserMapper.toUserDto(userRepository.save(userModel));
        }
        throw exception(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }

    /**
     * Change Password
     *
     * @param userDto
     * @param newPassword
     * @return
     */

    public UserDto changePassword(UserDto userDto, String newPassword) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if (user.isPresent()) {
            User userModel = user.get();
            userModel.setPassword(bCryptPasswordEncoder.encode(newPassword));
            return UserMapper.toUserDto(userRepository.save(userModel));
        }
        throw exception(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }

    /**
     * Returns a new RuntimeException
     *
     * @param entityType
     * @param exceptionType
     * @param args
     * @return
     */
    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return BRSException.throwException(entityType, exceptionType, args);
    }

}
