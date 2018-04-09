package com.satrumroom.controller;

import com.satrumroom.dto.FileInfoDTO;
import com.satrumroom.service.FileInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileInfoController {

    private final FileInfoService fileInfoService;

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        long userId = 1; //костыль

        FileInfoDTO uploadedFile = fileInfoService.upload(userId, file);

        if (uploadedFile == null) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
        }
        return "redirect:uploadStatus";

    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @GetMapping(value = "/download/{id}/{fileName}")
    public StreamingResponseBody download(HttpServletResponse response,
                                          @PathVariable(value = "id") long userId,
                                          @PathVariable(value = "fileName") String fileName) throws IOException {


        response.setContentType("video/mp4; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        response.setHeader("Content-Disposition", "attachment; filename=\""
                + fileName + ".mp4\"");

        InputStream inputStream = new FileInputStream(
                new File((fileInfoService.UPLOADED_FOLDER + userId + "/" + fileName + ".mp4")));
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                System.out.println("Writing some bytes..");
                outputStream.write(data, 0, nRead);
            }
        };
    }

}