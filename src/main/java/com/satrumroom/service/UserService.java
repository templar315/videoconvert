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
        UserDTO userDTO = null;
        if (user != null) {
            userDTO = UserDTO.builder()
                    .id(user.getId())
                    .login(user.getLogin())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .filesInfo(user.getFilesInfo().stream()
                                        .map(FileInfo::getId)
                                        .collect(Collectors.toList()))
                    .build();
        }
        return userDTO;
    }

    private User fromDTO(UserDTO userDTO) {
        User user = null;
        if (userDTO != null) {
            user = User.builder()
                    .id(userDTO.getId())
                    .login(userDTO.getLogin())
                    .password(userDTO.getPassword())
                    .role(userDTO.getRole())
                    .filesInfo(fileInfoRepository.findAllById(userDTO.getFilesInfo()))
                    .build();
        }
        return user;
    }

    public List<UserDTO> findAllByRole(String role) {
        return userRepository
                .findAllByRole(role)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findById(long id) {
        return toDTO(userRepository.getOne(id));
    }

    public UserDTO findByLogin(String login) {
        return toDTO(userRepository.findByLogin(login));
    }

    @Transactional
    public UserDTO add(UserDTO userDTO) {
        UserDTO userAdded = null;
        if(userRepository.existsById(userDTO.getId())) {
            userAdded = toDTO(userRepository.saveAndFlush(fromDTO(userDTO)));
        }
        return userAdded;
    }

    @Transactional
    public UserDTO update(UserDTO userDTO) {
        UserDTO userUpdated = null;
        if (userRepository.existsById(userDTO.getId())) {
            User userTemp = userRepository.getOne(userDTO.getId());
            User userNew = fromDTO(userDTO);
            userTemp = userTemp.toBuilder()
                    .login(userNew.getLogin())
                    .password(userNew.getPassword())
                    .role(userNew.getRole())
                    .filesInfo(userNew.getFilesInfo())
                    .build();
            userUpdated = toDTO(userRepository.saveAndFlush(userTemp));
        }
       return userUpdated;
    }

    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

}
