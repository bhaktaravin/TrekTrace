package travellog.demo.models;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {
  @Id
  private String id;
  private String userId;
  private String title;
  private String city;
  private String country;
  private String dateVisited; // ISO date string (or use Date)
  private Integer rating;
  private List<String> people;
  private String notes;
  private Location location;
  private List<String> photoUrls; // URLs or local file paths saved here
  private Long createdAt;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Location {
    private Double lat;
    private Double lng;
  }
}
