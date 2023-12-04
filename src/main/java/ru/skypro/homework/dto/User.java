import lombok.Data;

@Data
public class User {
    public Long id;
    public String email;
    public String firstName;
    public String lastName;
    public String phone;
    private String image;

    public User() {
    }
}
