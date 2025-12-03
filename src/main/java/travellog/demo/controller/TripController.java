package travellog.demo.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import travellog.demo.models.Photo;
import travellog.demo.models.Trip;
import travellog.demo.repositories.PhotoRepository;
import travellog.demo.repositories.TripRepository;
import travellog.demo.service.AppwriteService;

@RestController
@RequestMapping("/trips")
public class TripController {
      @Autowired TripRepository tripRepo;
  @Autowired PhotoRepository photoRepo;
  @Autowired AppwriteService appwriteService;

  // Create trip with optional photos
  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<?> createTrip(
    @RequestPart("metadata") TripMetadata metadata,
    @RequestPart(value = "photos", required = false) MultipartFile[] photos,
    @RequestHeader("Authorization") String authHeader) throws Exception {

    // Extract userId from authentication (set by JwtFilter)
    String userId = (String) org.springframework.security.core.context.SecurityContextHolder
                     .getContext().getAuthentication().getPrincipal();

    Trip trip = Trip.builder()
      .userId(userId)
      .title(metadata.getTitle())
      .city(metadata.getCity())
      .country(metadata.getCountry())
      .dateVisited(metadata.getDateVisited())
      .rating(metadata.getRating())
      .people(metadata.getPeople())
      .notes(metadata.getNotes())
      .location(metadata.getLocation())
      .createdAt(Instant.now().toEpochMilli())
      .build();

    List<String> fileIds = new ArrayList<>();
    if (photos != null) {
      for (MultipartFile file : photos) {
        String appwriteResp = appwriteService.uploadFile(file);
        // parse JSON to extract $id or the file ID. For brevity, we save whole response or extract id.
        // In production, use Jackson to parse and extract "$id" or "id"
        // Example hacky extraction (assumes JSON contains "$id":"..."):
        String id = extractIdFromJson(appwriteResp);
        fileIds.add(id);

        Photo p = Photo.builder()
          .appwriteFileId(id)
          .fileName(file.getOriginalFilename())
          .userId(userId)
          .createdAt(Instant.now().toEpochMilli())
          .build();
        photoRepo.save(p);
      }
    }

    trip.setPhotoIds(fileIds);
    tripRepo.save(trip);
    return ResponseEntity.ok(trip);
  }

  // helper DTO and extractor
  static class TripMetadata {
    private String title;
    private String city;
    private String country;
    private String dateVisited;
    private Integer rating;
    private List<String> people;
    private String notes;
    private Trip.Location location;
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getDateVisited() { return dateVisited; }
    public void setDateVisited(String dateVisited) { this.dateVisited = dateVisited; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public List<String> getPeople() { return people; }
    public void setPeople(List<String> people) { this.people = people; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Trip.Location getLocation() { return location; }
    public void setLocation(Trip.Location location) { this.location = location; }
  }

  private String extractIdFromJson(String json) {
    // replace with proper JSON parsing (Jackson). Quick regex:
    try {
      var m = java.util.regex.Pattern.compile("\"\\$id\"\\s*:\\s*\"([^\"]+)\"").matcher(json);
      if (m.find()) return m.group(1);
      m = java.util.regex.Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"").matcher(json);
      if (m.find()) return m.group(1);
    } catch (Exception ignored) {}
    return json; // fallback
  }

}
