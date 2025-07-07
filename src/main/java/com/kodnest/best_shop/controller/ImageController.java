package com.kodnest.best_shop.controller;

import com.kodnest.best_shop.dto.ImageDto;
import com.kodnest.best_shop.model.Image;
import com.kodnest.best_shop.response.ApiResponse;
import com.kodnest.best_shop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    /**
     * This ApiResponse is a user define class that we make in the response package.
     * The main function of this class is to return the message after completion of task or in case
     * of any error.
     */
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> files,@RequestParam Long productId) {
        try{
            List<ImageDto> imageDtos = imageService.saveImage(files,productId);
            return ResponseEntity.ok(new ApiResponse("upload successfully", imageDtos));
        }catch (Exception e) {
            return  ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("upload failed",e.getMessage()));
        }

    }


    @GetMapping("/image/download/{imageId}")
  public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) {
      try {
          Image image = imageService.getImageById(imageId);
          ByteArrayResource resource = new ByteArrayResource
                  (image.getImage().getBytes(1, (int) image.getImage().length()));

          return ResponseEntity.ok()
                  .contentType(MediaType.parseMediaType(image.getFileType()))
                  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                  .body(resource);
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
  }



  @PutMapping("/image/{imageId}/update")
  public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,@RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null) {
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("update success",null));
            }
        }catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("update failed",INTERNAL_SERVER_ERROR));
  }



    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("delete success",null));
            }
        }catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("delete failed",INTERNAL_SERVER_ERROR));

    }

}
