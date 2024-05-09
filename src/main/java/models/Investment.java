package models;

public class Investment {

    private int id;
    private String image, nom_invest,description_invest;

    public Investment() {
    }

    public Investment(String image, String nom_invest, String description_invest) {
        this.image = image;
        this.nom_invest = nom_invest;
        this.description_invest = description_invest;
    }

    public Investment(int id, String image, String nom_invest, String description_invest) {
        this.id = id;
        this.image = image;
        this.nom_invest = nom_invest;
        this.description_invest = description_invest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNom_invest() {
        return nom_invest != null ? nom_invest : "";
    }

    public void setNom_invest(String nom_invest) {
        this.nom_invest = nom_invest;
    }

    public String getDescription_invest() {
        return description_invest;
    }

    public void setDescription_invest(String description_invest) {
        this.description_invest = description_invest;
    }

    @Override
    public String toString() {
        return "Investment{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", nom_invest='" + nom_invest + '\'' +
                ", description_invest='" + description_invest + '\'' +
                '}';
    }
}
