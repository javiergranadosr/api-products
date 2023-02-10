package com.example.demo.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;

public interface IUploadFileService {
    Resource getFile(String subFolder, String filename) throws MalformedURLException;
    String saveFile(MultipartFile file, String subFolder);
    boolean deleteFile(String subFolder, String filename);
    Path getPath(String subFolder, String filename);
}
