package com.example.demo.services.impl;

import com.example.demo.services.IUploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    private final static String IMAGES_DIRECTORY = "images";
    private static final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);

    /**
     * Obtenemos el recurso (Archivo) con base a su nombre como parametro
     * @param filename
     * @return
     * @throws MalformedURLException
     */
    @Override
    public Resource getFile(String filename) throws MalformedURLException {
        Path pathFile = getPath(filename);
        Resource resource = new UrlResource(pathFile.toUri());
        if (!resource.exists() && !resource.isReadable()) {
            pathFile = Paths.get("src/main/resources/static/images").resolve("image_placeholder.jpg").toAbsolutePath();
            resource = new UrlResource(pathFile.toUri());
        }
        return resource;
    }

    /**
     * Guarda los archivos en la carpeta images
     * @param file
     * @return
     */
    @Override
    public String saveFile(MultipartFile file) {
        String originalFilename = UUID.randomUUID() + "_" + file.getOriginalFilename().replace(" ", "");
        Path pathFile = getPath(originalFilename);
        try {
            Files.copy(file.getInputStream(), pathFile);
        } catch (IOException e) {
            log.error("Error in copy file.");
            throw new RuntimeException(e);
        }
        return originalFilename;
    }

    /**
     * Eliminamos archivos
     * @param filename
     * @return
     */
    @Override
    public boolean deleteFile(String filename) {
        if (filename != null && filename.length() > 0) {
            Path prevPathFile = getPath(filename);
            File prevFile = prevPathFile.toFile();
            if (prevFile.exists() && prevFile.canRead()) {
                prevFile.delete();
                return true;
            }
        }
        return false;
    }

    /**
     * Obtenemos la ruta (Path) del archivo a travez de su nombre como parametro
     * @param filename
     * @return
     */
    @Override
    public Path getPath(String filename) {
        return Paths.get(IMAGES_DIRECTORY).resolve(filename).toAbsolutePath();
    }
}
