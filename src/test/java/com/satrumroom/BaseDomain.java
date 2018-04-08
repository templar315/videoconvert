package com.satrumroom;

import com.satrumroom.domain.FileInfo;
import com.satrumroom.domain.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;

import static java.time.LocalDateTime.now;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ComponentScan
public abstract class BaseDomain {

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() {

        User user = User.builder()
                .login("loginOne")
                .password("passOne")
                .role("User")
                .build();

        User user2 = User.builder()
                .login("loginTwo")
                .password("passTwo")
                .role("User")
                .build();

        FileInfo fileInfo1 = FileInfo.builder()
                .name("Title1")
                .path("some/path/file.avi")
                .lastChange(now())
                .videoFormat("AVI")
                .audioFormat("OGG")
                .convertible(true)
                .user(user)
                .build();

        FileInfo fileInfo2 = FileInfo.builder()
                .name("Title2")
                .path("some/path/file2.avi")
                .lastChange(now())
                .videoFormat("AVI")
                .audioFormat("OGG")
                .convertible(false)
                .user(user)
                .build();

        FileInfo fileInfo3 = FileInfo.builder()
                .name("Title3")
                .path("some/path/file3.avi")
                .lastChange(now())
                .videoFormat("AVI")
                .audioFormat("OGG")
                .convertible(true)
                .user(user2)
                .build();

        FileInfo fileInfo4 = FileInfo.builder()
                .name("Title4")
                .path("some/path/file4.avi")
                .lastChange(now())
                .videoFormat("AVI")
                .audioFormat("OGG")
                .convertible(false)
                .user(user2)
                .build();

        user.setFilesInfo(new ArrayList<>(Arrays.asList(fileInfo1, fileInfo2)));
        user2.setFilesInfo(new ArrayList<>(Arrays.asList(fileInfo3, fileInfo4)));

        entityManager.persist(user);
        entityManager.persist(user2);
    }

}
