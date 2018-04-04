package com.satrumroom.service;

import com.satrumroom.domain.FileInfo;
import com.satrumroom.dto.FileInfoDTO;
import com.satrumroom.repository.FileInfoRepository;
import com.satrumroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileInfoService {

    private final FileInfoRepository fileInfoRepository;

    private final UserRepository userRepository;

    private final String LOCAL_DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";

    private FileInfoDTO toDTO(FileInfo fileInfo) {
        FileInfoDTO fileInfoDTO = null;
        if (fileInfo != null) {
            String lastChangeStr = fileInfo.getLastChange()
                        .format(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN));
            fileInfoDTO = FileInfoDTO.builder()
                .id(fileInfo.getId())
                .name(fileInfo.getName())
                .path(fileInfo.getPath())
                .lastChange(lastChangeStr)
                .videoFormat(fileInfo.getVideoFormat())
                .audioFormat(fileInfo.getAudioFormat())
                .convertible(fileInfo.isConvertible())
                .user(fileInfo.getUser().getId())
                .build();
        }
        return fileInfoDTO;
    }

    private FileInfo fromDTO(FileInfoDTO fileInfoDTO) {
        FileInfo fileInfo = null;
        if (fileInfoDTO != null) {
            LocalDateTime lastChange = LocalDateTime.parse(
                    fileInfoDTO.getLastChange(),
                    DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN));
            fileInfo = FileInfo.builder()
                    .id(fileInfoDTO.getId())
                    .name(fileInfoDTO.getName())
                    .path(fileInfoDTO.getPath())
                    .lastChange(lastChange)
                    .videoFormat(fileInfoDTO.getVideoFormat())
                    .audioFormat(fileInfoDTO.getAudioFormat())
                    .convertible(fileInfoDTO.isConvertible())
                    .user(userRepository.getOne(fileInfoDTO.getUser()))
                    .build();
        }
        return fileInfo;
    }

    public List<FileInfoDTO> findAll() {
        return fileInfoRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FileInfoDTO add(FileInfoDTO fileInfoDTO) {
        FileInfoDTO fileInfoAdded = null;
        if(fileInfoRepository.existsById(fileInfoDTO.getId())) {
            fileInfoAdded = toDTO(fileInfoRepository.saveAndFlush(fromDTO(fileInfoDTO)));
        }
        return fileInfoAdded;
    }

    @Transactional
    public FileInfoDTO update(FileInfoDTO fileInfoDTO) {
        FileInfoDTO fileInfoUpdated = null;
        if (fileInfoRepository.existsById(fileInfoDTO.getId())) {
            FileInfo fileInfoTemp = fileInfoRepository.getOne(fileInfoDTO.getId());
            FileInfo fileInfoNew = fromDTO(fileInfoDTO);
            fileInfoTemp = fileInfoTemp.toBuilder()
                    .name(fileInfoNew.getName())
                    .path(fileInfoNew.getPath())
                    .lastChange(fileInfoNew.getLastChange())
                    .videoFormat(fileInfoNew.getVideoFormat())
                    .audioFormat(fileInfoNew.getAudioFormat())
                    .convertible(fileInfoNew.isConvertible())
                    .user(fileInfoNew.getUser())
                    .build();

            fileInfoUpdated = toDTO(fileInfoRepository.saveAndFlush(fileInfoTemp));
        }
        return fileInfoUpdated;
    }

    @Transactional
    public void delete(long id) {
        fileInfoRepository.deleteById(id);
    }

}