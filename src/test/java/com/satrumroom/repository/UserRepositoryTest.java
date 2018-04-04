package com.satrumroom.repository;

import com.satrumroom.BaseDomainTest;
import com.satrumroom.domain.FileInfo;
import com.satrumroom.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends BaseDomainTest {

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

    @Test
    public void update() {
        User user = User.builder()
                .login("loginThree")
                .password("passThree")
                .role("User3")
                .build();

        FileInfo fileInfo = FileInfo.builder()
                .name("Title1")
                .path("some/path/file.avi")
                .lastChange(new Date())
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

}
