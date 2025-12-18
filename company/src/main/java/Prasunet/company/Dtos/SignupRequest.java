package Prasunet.company.Dtos;

import Prasunet.company.User.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
