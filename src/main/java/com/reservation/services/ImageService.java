package com.reservation.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.reservation.models.Image;

public interface ImageService extends GeneralService<Image> {
	
	public Image uploadFileToS3Bucket(MultipartFile file) throws IOException ;
	public void deleteFileFromS3Bucket(String fileName, Long id);
	
}
