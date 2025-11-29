package com.project.sfm2025.entities;

public class UserViewJson {
    public Integer id;
    public String Vezetéknév;
    public String Keresztnév;
    public String Email;
    public boolean Tiltott;
    public String Jogosultság;

    public UserViewJson(Integer id,String vezetéknév, String keresztnév, String email, boolean tiltott, String jogosultság) {
        this.id = id;
        this.Vezetéknév = vezetéknév;
        this.Keresztnév = keresztnév;
        this.Email = email;
        this.Tiltott = !tiltott;
        this.Jogosultság = jogosultság;
    }
}
