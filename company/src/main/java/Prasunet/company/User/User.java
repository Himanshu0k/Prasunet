package Prasunet.company.User;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;              // Unique ID (MongoDB ObjectId)

    private String name;            // Full name

    private String email;           // Login email (unique)

    private String password;    // Hashed password (BCrypt)

    private Role role;            // STUDENT, MENTOR, ADMIN

    private boolean approved;       // Used for mentor approval

    private Instant createdAt;       // User creation date
}

