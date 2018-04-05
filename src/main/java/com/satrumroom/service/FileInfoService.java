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
        if (fileInfo != null) {
            String lastChangeStr = fileInfo.getLastChange()
                        .format(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN));
            return FileInfoDTO.builder()
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
        return null;
    }

    private FileInfo fromDTO(FileInfoDTO fileInfoDTO) {
        if (fileInfoDTO != null) {
            LocalDateTime lastChange = LocalDateTime.parse(
                    fileInfoDTO.getLastChange(),
                    DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN));
            return FileInfo.builder()
                    .id(fileInfoDTO.getId())
                    .name(fileInfoDTO.getName())
                    .path(fileInfoDTO.getPath())
                    .lastChange(lastChange)
                    .videoFormat(fileInfoDTO.getVideoFormat())
                    .audioFormat(fileInfoDTO.getAudioFormat())
                    .convertible(fileInfoDTO.isConvertible())
                    .user(fileInfoDTO.getUser() > 0L
                            ? userRepository.getOne(fileInfoDTO.getUser())
                            : null)
                    .build();
        }
        return null;
    }

    @Transactional
    public FileInfoDTO add(FileInfoDTO fileInfoDTO) {
        if(!fileInfoRepository.existsById(fileInfoDTO.getId())) {
            return toDTO(fileInfoRepository.saveAndFlush(fromDTO(fileInfoDTO)));
        }
        return null;
    }

    @Transactional
    public FileInfoDTO update(FileInfoDTO fileInfoDTO) {
        if (fileInfoRepository.existsById(fileInfoDTO.getId())) {
            FileInfo fileInfoTemp = fileInfoRepository.getOne(fileInfoDTO.getId());
            fileInfoTemp.setName(fileInfoDTO.getName());
            fileInfoTemp.setPath(fileInfoDTO.getPath());
            fileInfoTemp.setLastChange(LocalDateTime.parse(
                    fileInfoDTO.getLastChange(),
                    DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN)));
            fileInfoTemp.setVideoFormat(fileInfoDTO.getVideoFormat());
            fileInfoTemp.setAudioFormat(fileInfoDTO.getAudioFormat());
            fileInfoTemp.setConvertible(fileInfoDTO.isConvertible());
            return toDTO(fileInfoRepository.saveAndFlush(fileInfoTemp));
        }
        return null;
    }

    @Transactional
    public void delete(long id) {
        fileInfoRepository.deleteById(id);
    }

    public List<FileInfoDTO> getAllById(List<Long> idList) {
        if(idList != null) {
            return fileInfoRepository
                    .findAllById(idList)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }
        return null;
    }

}