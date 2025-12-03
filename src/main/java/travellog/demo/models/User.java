package travellog.demo.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id; 
    private String email;
    private String passwordHash;
    private String displayName;
    private long createdAt;
}
