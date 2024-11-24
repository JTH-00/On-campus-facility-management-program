package com.example.demo.user.service;

import com.example.demo.DataNotFoundException;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;


    private UserDTO of(User user) {
        return this.modelMapper.map(user, UserDTO.class);
    }



    private User of(UserDTO userDTO) {
        return this.modelMapper.map(userDTO,User.class);
    }

    public UserDTO userdb(String username, String userid, String userpw, String userphone, String useremail) {
        User user = new User();
        user.setUsername(username);
        user.setUserid(userid);
        user.setUserpw(passwordEncoder.encode(userpw));
        user.setUseremail(useremail);
        user.setUserphone(userphone);
        this.userRepository.save(user);
        return of (user);
    }


    public UserDTO getUserDto(String userid) {
        Optional<User> user = this.userRepository.findByUserid(userid);
        if (user.isPresent()) {
            return of(user.get());
        } else {
            throw new DataNotFoundException("user not found");
        }
    }

    public User getUser(String userid) {
        Optional<User> user = this.userRepository.findByUserid(userid);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }


    public UserDTO modify(UserDTO userDTO, String username,String userphone, String useremail) {
        userDTO.setUsername(username);
        userDTO.setUserphone(userphone);
        userDTO.setUseremail(useremail);
        this.userRepository.save(of(userDTO));
        return userDTO;
    }

    public void delete(UserDTO userdto){
        userRepository.deleteById(userdto.getUserhash());
    }


    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String userid){
        return userRepository.findByUserid(userid);
    }


    public long getUserCount() {

        return userRepository.count();
    }
}
