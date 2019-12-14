package com.abedo.firebasepushnotifications;

public class NotifyUsers extends UserId{
    String name , image;

    public NotifyUsers(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public NotifyUsers() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "NotifyUsers{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
