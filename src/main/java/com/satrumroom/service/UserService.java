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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    private final FileInfoRepository fileInfoRepository;

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
        if(!userRepository.existsById(userDTO.getId())) {
            String role = userDTO.getRole();
            if(role != null) {
                if(role.equals("ADMIN") || role.equals("USER"))
                {
                    return toDTO(userRepository.saveAndFlush(fromDTO(userDTO)));
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
        userRepository.deleteById(id);
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
        if(login != null) {
            return toDTO(userRepository.findByLogin(login));
        }
        return null;
    }

    public List<UserDTO> findAllByRole(String role) {
        if(role != null) {
            return userRepository.findAllByRole(role)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }
        return null;
    }

}
