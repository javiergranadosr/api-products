package com.example.demo.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;

public interface IUploadFileService {
    Resource getFile(String filename) throws MalformedURLException;
    String saveFile(MultipartFile file);
    boolean deleteFile(String filename);
    Path getPath(String filename);
}
