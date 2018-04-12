package com.satrumroom.service;

import com.satrumroom.BaseDomain;
import com.satrumroom.dto.UserDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ComponentScan
public class UserServiceTest extends BaseDomain {

    @Autowired
    private UserService userService;

    @Test
    public void add() {
        UserDTO userDTO = UserDTO.builder()
                .login("UserLogin")
                .password("qwerty")
                .role("ADMIN")
                .build();

        assertThat(userService.getAll()).hasSize(2);
        userService.add(userDTO);
        assertThat(userService.getAll()).hasSize(3);
    }

    @Test()
    public void createNotValidUser() {
        userService.add(UserDTO.builder()
                .login("loginThree")
                .password(null)
                .role("ADMIN")
                .build());
        assertThat(userService.getAll()).hasSize(2);
    }

    @Test
    public void findUserByLogin(){
        UserDTO user = userService.findByLogin("loginOne");
        assertThat(user).isNotNull();
    }

    @Test
    public void findUserById(){
        UserDTO user = userService.getOne(userService.getAll().get(0).getId());
        assertThat(user).isNotNull();
    }

    @Test
    public void findUsersByRole(){
        List<UserDTO> users = userService.findAllByRole("User");
        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
    }


    @Test
    public void findUsersByNotValidRole(){
        List<UserDTO> users = userService.findAllByRole("aaaaa");
        assertThat(users).hasSize(0);
    }


    @Test
    public void findNotExistsUser(){
      UserDTO user = userService.findByLogin("QEWEWQ");
      assertThat(user).isNull();
    }

    @Test
    public void update() {
        UserDTO user = userService.getAll().get(0);
        user = user.toBuilder().login("the best").build();
        userService.update(user);
        assertThat(userService.findByLogin("the best")).isNotNull();
    }

    @Test
    public void getAll() {
        assertThat(userService.getAll()).hasSize(2);
    }

}
