package koo.securityv2jwt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String password;
    private String roles; // USER, ADMIN

    public List<String> getRoleList() {
        if (this.getRoles().length() > 0) {
            return Arrays.asList(this.getRoles().split(","));
        }

        return new ArrayList<>();
    }

}
