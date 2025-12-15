package travellog.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Document(collection = "photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Photo {

    @Id
    private String id;
    private String tripId;
    private String userId; 
    // Removed appwriteFileId (Appwrite dependency)
    private String fileName;
    private String url;
    private long createdAt;
    
}
