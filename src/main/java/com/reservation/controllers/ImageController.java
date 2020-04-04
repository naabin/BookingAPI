package com.reservation.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.reservation.exception.ResourceNotFoundException;
import com.reservation.models.Image;
import com.reservation.models.Restaurant;
import com.reservation.services.ImageService;
import com.reservation.services.RestaurantService;

@RestController
@RequestMapping("/api/image")
public class ImageController {
	
	private final ImageService imageService;
	private final RestaurantService restaurantService;
	
	public ImageController(ImageService imageService, RestaurantService restaurantService) {
		this.imageService = imageService;
		this.restaurantService = restaurantService;
	}
	
	@PostMapping
	public ResponseEntity<Image> uploadImageToRestaurant(
			@RequestPart(value = "file") MultipartFile file,
			@RequestParam(name = "restaurantId", required = true)Long id) throws ResourceNotFoundException, IOException{
		Restaurant restaurant = this.restaurantService.findRestaurantById(id).orElseThrow(() -> new ResourceNotFoundException("Resource could not be found"));
		Image image = this.imageService.uploadFileToS3Bucket(file);
		image.setRestaurant(restaurant);
		restaurant.setImage(image);
		this.restaurantService.updateRestaurant(restaurant);
		Image updatedImage = this.imageService.update(image);
		
		return ResponseEntity.ok().body(updatedImage);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteImage(
			@PathVariable("id") Long id,
			@RequestParam(name = "imageURL", required = true)String imageUrl){
		this.imageService.deleteFileFromS3Bucket(imageUrl, id);
		return ResponseEntity.ok().build();
	}
}
