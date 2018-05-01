package ie.ncirl.a14445618.theintelligentfoodnetwork;

/**
 * Created by Leon on 30/04/2018.
 */

public class ModelUser {
    String name;
    String weight;
    String email;
    String joined;
    String phone;

    public ModelUser() {
    }


    public ModelUser(String name, String weight, String email, String joined, String phone) {
        this.name = name;
        this.weight = weight;
        this.email = email;
        this.joined = joined;
        this.phone = phone;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
