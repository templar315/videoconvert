package com.satrumroom.repository;

import com.satrumroom.BaseDomain;
import com.satrumroom.domain.FileInfo;
import com.satrumroom.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

public class FileInfoRepositoryTest extends BaseDomain {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Test
    public void add() {
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

    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    public void createNotValidFileInfo() {
        FileInfo fileInfo = FileInfo.builder()
                .name("Title1")
                .path("some/path/file5.avi")
                .lastChange(now())
                .videoFormat("AVI")
                .audioFormat("OGG")
                .convertible(true)
                .build();
        assertThat(fileInfoRepository.findAll()).hasSize(4);
        fileInfoRepository.saveAndFlush(fileInfo);
        assertThat(fileInfoRepository.findAll()).hasSize(4);
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

    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void deleteNotExistsFileInfo(){
        assertThat(fileInfoRepository.findAll()).hasSize(4);
        fileInfoRepository.deleteById(1234L);
        assertThat(fileInfoRepository.findAll()).hasSize(4);
    }

    @Test
    public void findById() {
        long id = fileInfoRepository.findAll().get(0).getId();
        FileInfo fileInfo = fileInfoRepository.getOne(id);
        assertThat(fileInfo).isNotNull();
        assertThat(fileInfo.getId()).isEqualTo(id);
    }

    @Test
    public void findAll() {
        List<FileInfo> infoList = fileInfoRepository.findAll();
        assertThat(infoList).hasSize(4);
    }

}
