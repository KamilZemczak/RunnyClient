package kamilzemczak.runny.helper;

/**
 * Created by Kamil Zemczak on 10.02.2018.
 */

public class MySingleton {

    private static MySingleton mySingleton;

    private String currentName;

    private Integer weight;
    private Integer height;
    private String city;
    private String about;

    public static synchronized MySingleton getInstance() {
        if (mySingleton == null)
            mySingleton = new MySingleton();
        return mySingleton;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }


    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}