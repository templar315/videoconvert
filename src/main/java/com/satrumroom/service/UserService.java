package com.satrumroom.service;

import com.satrumroom.domain.FileInfo;
import com.satrumroom.domain.User;
import com.satrumroom.dto.UserDTO;
import com.satrumroom.repository.FileInfoRepository;
import com.satrumroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final FileInfoService fileInfoService;

    private final UserRepository userRepository;

    private final FileInfoRepository fileInfoRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserDTO toDTO(User user) {
        if (user != null) {
            return UserDTO.builder()
                    .id(user.getId())
                    .login(user.getLogin())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .filesInfo(user.getFilesInfo() != null
                            ? user.getFilesInfo().stream()
                            .map(FileInfo::getId)
                            .collect(Collectors.toList())
                            : null)
                    .build();
        }
        return null;
    }

    private User fromDTO(UserDTO userDTO) {
        if (userDTO != null) {
            return User.builder()
                    .id(userDTO.getId())
                    .login(userDTO.getLogin())
                    .password(userDTO.getPassword())
                    .role(userDTO.getRole())
                    .filesInfo(userDTO.getFilesInfo() != null
                            ? fileInfoRepository.findAllById(userDTO.getFilesInfo())
                            : null)
                    .build();
        }
        return null;
    }

    @Transactional
    public UserDTO add(UserDTO userDTO) {
        if (!userRepository.existsById(userDTO.getId())) {
            String login = userDTO.getLogin();
            String password = userDTO.getPassword();
            String role = userDTO.getRole();
            if (login != null && password != null && role != null) {
                if (login.length() > 0 && password.length() > 0) {
                    userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
                    if (role.equals("ADMIN")) {
                        return toDTO(userRepository.saveAndFlush(fromDTO(userDTO)));
                    }
                    if (role.equals("USER")) {
                        return toDTO(userRepository.saveAndFlush(fromDTO(userDTO)));
                    }
                }
            }
        }
        return null;
    }

    @Transactional
    public UserDTO update(UserDTO userDTO) {
        if (userRepository.existsById(userDTO.getId())) {
            User userTemp = userRepository.getOne(userDTO.getId());
            userTemp.setLogin(userDTO.getLogin());
            userTemp.setPassword(userDTO.getPassword());
            return toDTO(userRepository.saveAndFlush(userTemp));
        }
        return null;
    }

    @Transactional
    public void delete(long id) {
        if (userRepository.existsById(id)) {
            List<FileInfo> allUserFiles =
                    userRepository.getOne(id).getFilesInfo();
            if (allUserFiles.size() > 0) {
                for (FileInfo fileInfo : allUserFiles) {
                    if (new File(fileInfo.getPath()).delete()) {
                        fileInfoRepository.deleteById(fileInfo.getId());
                    } else {
                        return;
                    }
                }
            }
            new File(fileInfoService.UPLOADED_FOLDER + id).delete();
            userRepository.deleteById(id);
        }
    }

    public UserDTO getOne(long id) {
        return toDTO(userRepository.getOne(id));
    }

    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findByLogin(String login) {
        if (login != null) {
            return toDTO(userRepository.findByLogin(login));
        }
        return null;
    }

    public List<UserDTO> findAllByRole(String role) {
        if (role != null) {
            return userRepository.findAllByRole(role)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }
        return null;
    }

}
