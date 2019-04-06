package com.example.bloodbank.Model;

public class UserInformation {

    String name;
    String type;
    String identification;
    String nationality;
    String birth_date;
    String blood_type;
    String phonenumber;
    String account;
    String adress;
    String password;


    public UserInformation(String name, String type, String identification, String nationality, String birth_date, String blood_type, String phonenumber, String account, String adress, String password) {
        this.name = name;
        this.type = type;
        this.identification = identification;
        this.nationality = nationality;
        this.birth_date = birth_date;
        this.blood_type = blood_type;
        this.phonenumber = phonenumber;
        this.account = account;
        this.adress = adress;
        this.password = password;
    }

    public UserInformation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
