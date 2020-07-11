package merchstore.com.classifieds;

import java.util.ArrayList;

public class User {
    private String name;
    private String phone;
    private String email;
    private String password;
    private String city;
    private String image;
    private ArrayList<String> interests;

    public User(String name, String phone, String email, String password, String city, String image, ArrayList<String> interests) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.city = city;
        this.image = image;
        this.interests = interests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
