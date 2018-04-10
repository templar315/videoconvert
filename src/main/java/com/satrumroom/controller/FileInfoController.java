package com.satrumroom.controller;

import com.satrumroom.dto.FileInfoDTO;
import com.satrumroom.dto.UserDTO;
import com.satrumroom.service.FileInfoService;
import com.satrumroom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileInfoController {

    private final UserService userService;

    private final FileInfoService fileInfoService;

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDTO user = userService.findByLogin(auth.getName());
            long userId = user.getId();
            fileInfoService.upload(userId, file);
        } catch(Exception ex) {

        }
        return "redirect:/user";
    }

    @GetMapping(value = "/download/{fileId}")
    public StreamingResponseBody download(HttpServletResponse response,
                                          @PathVariable(value = "fileId") long fileId) throws IOException {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDTO user = userService.findByLogin(auth.getName());
            long userId = user.getId();

            FileInfoDTO fileInfo = fileInfoService.getOne(fileId);
            String fileName = fileInfo.getName();
            String extension = fileName.split("\\.")[1];
            response.setContentType("video/" + extension + "; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + fileName + "\"");

            InputStream inputStream = new FileInputStream(
                    new File((fileInfoService.UPLOADED_FOLDER + userId + "/" + fileName)));
            return outputStream -> {
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, nRead);
                }
            };
        } catch(Exception ex){
            return null;
        }
    }

    @GetMapping(value = "/delete/{fileId}")
    public String delete(@PathVariable(value = "fileId") long fileId) {
        try {
            fileInfoService.delete(fileId);
        } catch(Exception ex) {
        }
        return "redirect:/user";
    }

}