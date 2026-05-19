package com.renan.refyne.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUploadService {

  private final Cloudinary cloudinary;

  public FileUploadService(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  public String uploadFile(MultipartFile file, String folder) throws IOException {
    if (file == null || file.isEmpty()) {
      return null;
    }

    String originalFilename = file.getOriginalFilename();
    String publicId = UUID.randomUUID().toString();

    if (originalFilename != null && originalFilename.contains(".")) {
      // Remove extension as Cloudinary adds it if resource_type is raw, or handles it automatically
      publicId = UUID.randomUUID().toString() + "_" + originalFilename.substring(0, originalFilename.lastIndexOf('.'));
    }

    Map<String, Object> params = ObjectUtils.asMap(
      "folder", "refyne/" + folder,
      "public_id", publicId,
      "resource_type", "auto" // Automatically detect image vs pdf
    );

    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

    return uploadResult.get("secure_url").toString();
  }

  public String uploadStartupLogo(MultipartFile file) throws IOException {
    return uploadFile(file, "startup_logos");
  }

  public String uploadCandidateResume(MultipartFile file) throws IOException {
    return uploadFile(file, "candidate_resumes");
  }
}
