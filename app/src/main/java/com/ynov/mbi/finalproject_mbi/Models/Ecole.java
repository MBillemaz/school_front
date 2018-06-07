package com.ynov.mbi.finalproject_mbi.Models;

import java.io.Serializable;

public class Ecole implements Serializable {

    private Integer id;
    private String name;
    private String address;
    private int nb_student;
    private String status;
    private double latitude;
    private double longitude;
    private String mail;
    private String postal_code;
    private String horaire;
    private String phone_number;
    private String commune;

    public Ecole(String name, String address, int nb_student, String status, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.nb_student = nb_student;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Ecole(String name, String address, int nb_student, String status, double latitude, double longitude, String mail, String postal_code, String horaire, String phone_number, String commune) {
        this.id = null;
        this.name = name;
        this.address = address;
        this.nb_student = nb_student;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mail = mail;
        this.postal_code = postal_code;
        this.horaire = horaire;
        this.phone_number = phone_number;
        this.commune = commune;
    }

    public Ecole(int id, String name, String address, int nb_student, String status, double latitude, double longitude, String mail, String postal_code, String horaire, String phone_number, String commune) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.nb_student = nb_student;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mail = mail;
        this.postal_code = postal_code;
        this.horaire = horaire;
        this.phone_number = phone_number;
        this.commune = commune;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNb_student() {
        return nb_student;
    }

    public void setNb_student(int nb_student) {
        this.nb_student = nb_student;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Ecole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", nb_student=" + nb_student +
                ", status='" + status + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", mail='" + mail + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", horaire='" + horaire + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", commune='" + commune + '\'' +
                '}';
    }
}
