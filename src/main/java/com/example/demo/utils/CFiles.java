package com.example.demo.utils;

import com.example.demo.services.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
public class CFiles {
    @Autowired
    private IUploadFileService service;

    public HttpHeaders getHeaders(Resource resource) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        return headers;
    }

    public Resource getResource(String pathFolder, String filename){
        Resource resource = null;
        try {
            resource = this.service.getFile(pathFolder, filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return resource;
    }
}
