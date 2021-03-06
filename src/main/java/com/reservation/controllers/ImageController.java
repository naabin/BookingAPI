package com.reservation.controllers;


import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		restaurant.setImage(image);
		image.setRestaurant(restaurant);
		this.restaurantService.updateRestaurant(restaurant);
		Image updatedImage = this.imageService.update(image);
		
		return ResponseEntity.ok().body(updatedImage);
	}
	
	@GetMapping
	public ResponseEntity<Image> getImage(@RequestParam(name = "restaurantId", value = "restaurantId", required = true)Long restaurantId) throws ResourceNotFoundException{
		Image image = this.imageService.getImageByRestaurantId(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Resource could not be found"));
		return ResponseEntity.ok().body(image);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteImage(
			@PathVariable("id") Long id,
			@RequestParam(name = "imageURL", required = true)String imageUrl,
			@RequestParam(name = "restaurantId", required = true)Long restuarantId) throws ResourceNotFoundException{
		Restaurant restaurant = this.restaurantService.findRestaurantById(restuarantId).orElseThrow(() ->  new ResourceNotFoundException("Could not locate the resource"));
		restaurant.setImage(null);
		this.restaurantService.updateRestaurant(restaurant);
		this.imageService.deleteFileFromS3Bucket(imageUrl, id);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{imageId}")
	public ResponseEntity<Image> updateImage(
			@PathVariable("imageId") Long currentImageUrlId,
			@RequestParam(name = "restaurantId", required = true) long restaurantId,
			@RequestPart(value = "file") MultipartFile file,
			@RequestParam(name = "currentImageUrl", required = true)String currentImageUrl
			) throws IOException, ResourceNotFoundException{
		Restaurant restaurant = this.restaurantService.findRestaurantById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Could not find the resources"));
		restaurant.setImage(null);
		Image image = this.imageService.changePicture(file, currentImageUrl, currentImageUrlId);
		restaurant.setImage(image);
		image.setRestaurant(restaurant);
		this.restaurantService.updateRestaurant(restaurant);
		Image updatedImage = this.imageService.update(image);
		return ResponseEntity.ok().body(updatedImage);
		
	}
}
