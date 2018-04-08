package com.satrumroom.repository;

import com.satrumroom.BaseDomain;
import com.satrumroom.domain.FileInfo;
import com.satrumroom.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends BaseDomain {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Test
    public void add() {
        userRepository.saveAndFlush(User.builder()
                .login("loginThree")
                .password("passThree")
                .role("User3")
                .build());

        assertThat(userRepository.findByLogin("loginThree")).isNotNull();
        assertThat(userRepository.findAll()).hasSize(3);
    }

    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    public void createNotValidUser() {
        userRepository.saveAndFlush(User.builder()
                .login("loginThree")
                .role("User3")
                .build());
    }

    @Test
    public void update() {
        User user = User.builder()
                .login("loginThree")
                .password("passThree")
                .role("User3")
                .build();

        FileInfo fileInfo = FileInfo.builder()
                .name("Title1")
                .path("some/path/file5.avi")
                .lastChange(now())
                .videoFormat("AVI")
                .audioFormat("OGG")
                .convertible(true)
                .user(user)
                .build();

        user.setFilesInfo(new ArrayList<>(Arrays.asList(fileInfo)));

        fileInfoRepository.saveAndFlush(fileInfo);

        assertThat(userRepository.findByLogin("loginThree")).isNotNull();
        assertThat(userRepository.findAll()).hasSize(3);

        User user2 = userRepository.findByLogin("loginThree");
        user2.setLogin("newLogin");
        userRepository.saveAndFlush(user2);

        assertThat(userRepository.findByLogin("newLogin")).isNotNull();
        assertThat(userRepository.findAll()).hasSize(3);
    }

    @Test
    public void delete() {
        assertThat(userRepository.findAll()).hasSize(2);
        assertThat(fileInfoRepository.findAll()).hasSize(4);

        userRepository.delete(userRepository.findByLogin("loginOne"));

        assertThat(userRepository.findByLogin("loginOne")).isNull();
        assertThat(fileInfoRepository.findAll()).hasSize(2);
        assertThat(userRepository.findAll()).hasSize(1);
    }

    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void deleteNotExistsUser(){
        assertThat(userRepository.findAll()).hasSize(2);
        userRepository.deleteById(111111L);
        assertThat(userRepository.findAll()).hasSize(2);
    }

    @Test
    public void findAllByRole() {
        List<User> users = userRepository.findAllByRole("User");
        assertThat(users).hasSize(2);
    }

    @Test
    public void findByLogin() {
        User user = userRepository.findByLogin("loginOne");
        assertThat(user).isNotNull();
        assertThat(user.getLogin()).isEqualTo("loginOne");
    }

    @Test
    public void findById() {
        long id = userRepository.findAll().get(0).getId();
        User user = userRepository.getOne(id);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    public void findAll() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

}
