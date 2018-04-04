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

public class FileInfoRepositoryTest extends BaseDomainTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileInfoRepository fileInfoRepository;


    public void start() {
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
    }

    @Test
    public void add() {
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

        assertThat(fileInfoRepository.existsById(userRepository
                .findByLogin("loginThree")
                .getFilesInfo()
                .get(0)
                .getId()))
                .isTrue();
        assertThat(fileInfoRepository.getOne(userRepository
                .findByLogin("loginThree")
                .getFilesInfo()
                .get(0)
                .getId())
                .getName())
                .isEqualTo("Title1");
        assertThat(fileInfoRepository.findAll()).hasSize(5);
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

        assertThat(fileInfoRepository.existsById(userRepository
                .findByLogin("loginThree")
                .getFilesInfo()
                .get(0)
                .getId()))
                .isTrue();
        assertThat(fileInfoRepository.getOne(userRepository
                .findByLogin("loginThree")
                .getFilesInfo()
                .get(0)
                .getId())
                .getName())
                .isEqualTo("Title1");
        assertThat(fileInfoRepository.findAll()).hasSize(5);

        FileInfo fileInfo1 = fileInfoRepository.getOne(userRepository
                .findByLogin("loginThree")
                .getFilesInfo()
                .get(0)
                .getId());
        fileInfo1.setName("NewTitle");

        fileInfoRepository.saveAndFlush(fileInfo1);

        assertThat(fileInfoRepository.existsById(userRepository
                .findByLogin("loginThree")
                .getFilesInfo()
                .get(0)
                .getId())).isTrue();
        assertThat(fileInfoRepository.getOne(userRepository
                .findByLogin("loginThree")
                .getFilesInfo()
                .get(0)
                .getId()).getName()).isEqualTo("NewTitle");
        assertThat(fileInfoRepository.findAll()).hasSize(5);
    }

    @Test
    public void delete() {
        assertThat(userRepository.findAll()).hasSize(2);
        assertThat(fileInfoRepository.findAll()).hasSize(4);

        User user1 = userRepository.findByLogin("loginOne");
        user1.getFilesInfo().remove(0);
        userRepository.saveAndFlush(user1);

        assertThat(userRepository.findAll()).hasSize(2);
        assertThat(fileInfoRepository.findAll()).hasSize(3);
    }

}
