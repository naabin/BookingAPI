package com.reservation.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.reservation.models.Image;
import com.reservation.repositories.ImageRepository;
import com.reservation.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
	
	@Value("${endpoint}")
	private String endPoint;
	
	@Value("${bucket}")
	private String bucket;
	
	@Value("${credentials.access-key}")
	private String accessKey;
	
	@Value("${credentials.secret-key}")
	private String secretKey;
		
	
	private final ImageRepository imageRepository;
	

	
	private AmazonS3 amazonS3;
	
	public ImageServiceImpl(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}
	
	
	@PostConstruct
	private void amazonCredentials() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.amazonS3 = AmazonS3ClientBuilder.
				standard().
				withCredentials(new AWSStaticCredentialsProvider(credentials))
				.build();
	}
	

	@Override
	public Image save(Image t) {
		return null;
	}

	@Override
	public Optional<Image> findById(long id) {
		return this.imageRepository.findById(id);
	}

	@Override
	public Image update(Image image) {
		return this.imageRepository.saveAndFlush(image);
	}

	@Override
	public void delete(long id) {
		this.imageRepository.deleteById(id);
	}

	@Async
	@Override
	public Image uploadFileToS3Bucket(MultipartFile file) throws IOException {
		String fileUrl = "";
			File convertMultipartFile = convertMultipartFile(file);
			String generatedFileName = generateFileName(file);
			fileUrl = this.endPoint + "/" + this.bucket + "/" + generatedFileName;
			uploadFile(generatedFileName, convertMultipartFile);
			convertMultipartFile.delete();
			Image uploadedImage = new Image(fileUrl, OffsetDateTime.now());
			Image savedImage = this.imageRepository.save(uploadedImage);
			return savedImage;
	}

	@Async
	@Override
	public void deleteFileFromS3Bucket(String fileName, Long id) {
		String file = fileName.substring(fileName.lastIndexOf("/")  +1);
		this.amazonS3.deleteObject(new DeleteObjectRequest(this.bucket, file));
		this.delete(id);
	}
	
	private File convertMultipartFile(MultipartFile file) throws IOException {
		File convertedFile = new File(file.getOriginalFilename());
		FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
		fileOutputStream.write(file.getBytes());
		fileOutputStream.close();
		return convertedFile;
		
	}
	
	private String generateFileName(MultipartFile file) {
		return new Date().getTime() + "-" + file.getOriginalFilename().replace(" ", "_");
	}
	
	private void uploadFile(String fileName, File file) {
		this.amazonS3.putObject(new PutObjectRequest(this.bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

}
