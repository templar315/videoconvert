package com.satrumroom.service;

import com.satrumroom.domain.FileInfo;
import com.satrumroom.dto.FileInfoDTO;
import com.satrumroom.repository.FileInfoRepository;
import com.satrumroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileInfoService {

    public final String UPLOADED_FOLDER = "./userfiles/";

    private final List<String> FILE_EXTENSIONS = Arrays.asList(
            "wmv", "wmp", "wm", "asf", "smk", "bik", "fli", "flc", "flic",
            "dsm", "dsv", "dsa", "dss", "ivf", "swf", "divx", "amv",
            "avi", "mpg", "mpeg", "mpe", "m1v", "m2v", "mpv2", "mp2v",
            "pva", "evo", "ts", "tp", "trp", "m2t", "m2ts", "mts", "rec",
            "ssif", "vob", "ifo", "mkv", "mk3d", "webm",
            "mp4", "m4v", "mp4v", "hdmov",
            "mov", "3gp", "3gpp", "3ga", "3g2", "flv", "f4v", "ogm", "ogv",
            "rm", "rmvb", "rt", "ram", "rpm", "rmm", "rp", "smi", "smil");

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
        if(fileInfoRepository.existsById(id)) {
            new File(fileInfoRepository.getOne(id).getPath()).delete();
            fileInfoRepository.deleteById(id);
        }
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

    public FileInfoDTO upload (long userId, MultipartFile file) {

        FileInfoDTO uploadedFile = null;

        if (file.isEmpty()) {
            return null;
        }

        File checkFile = new File(UPLOADED_FOLDER + userId
                + "/" + file.getOriginalFilename());
        if (checkFile.exists()) {
            return null;
        }

        if (!FILE_EXTENSIONS.contains(file.getOriginalFilename().split("\\.")[1])) {
            return null;
        }

        try {

            File dir = new File(UPLOADED_FOLDER + userId);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + userId
                    + "/" + file.getOriginalFilename());
            Files.write(path, bytes);

            uploadedFile = FileInfoDTO.builder()
                    .name(file.getOriginalFilename())
                    .path(UPLOADED_FOLDER + userId
                            + "/" + file.getOriginalFilename())
                    .lastChange(LocalDateTime.now()
                            .format(DateTimeFormatter
                                    .ofPattern(LOCAL_DATE_TIME_PATTERN)))
                    .videoFormat("unknown")
                    .audioFormat("unknown")
                    .convertible(true)
                    .user(userId)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return add(uploadedFile);

    }

    public FileInfoDTO getOne(long id) {
        return toDTO(fileInfoRepository.getOne(id));
    }

}