package com.ynov.mbi.finalproject_mbi.Models;

import java.util.List;

//L'api peut nous renvoyer une école, une liste d'école ou une erreur.
//Tous les cas sont traités dans cette classe
public class JsonResponseEcole {
    private List<Ecole> schools;
    private Ecole school;
    private String error;

    public JsonResponseEcole(List<Ecole> schools) {
        this.schools = schools;
    }

    public JsonResponseEcole(Ecole school) {
        this.school = school;
    }

    public JsonResponseEcole(String error) {
        this.error = error;
    }

    public List<Ecole> getSchools() {
        return schools;
    }

    public void setSchools(List<Ecole> schools) {
        this.schools = schools;
    }

    public Ecole getSchool() {
        return school;
    }

    public void setSchool(Ecole school) {
        this.school = school;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
