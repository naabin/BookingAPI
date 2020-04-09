package com.reservation.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.reservation.models.Image;

public interface ImageService extends GeneralService<Image> {
	
	public Image uploadFileToS3Bucket(MultipartFile file) throws IOException ;
	public void deleteFileFromS3Bucket(String fileName, Long id);
	
	public Image changePicture(MultipartFile file, String currentImageUrl, Long currentImageUrlId) throws IOException;
	
	public Optional<Image> getImageByRestaurantId(Long restaurantId);
}
