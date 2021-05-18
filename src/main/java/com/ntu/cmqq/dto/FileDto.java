package com.ntu.cmqq.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Arya
 */
public class FileDto {
    private MultipartFile file;
    private String name;
    private int teachId;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeachId() {
        return teachId;
    }

    public void setTeachId(int teachId) {
        this.teachId = teachId;
    }
}
