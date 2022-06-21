package com.example.fcmusicapp;

public class Cancion {
    private String nombre;
    private int duracion;
    private int id;
    private boolean like;

    public Cancion(int id, String nombre, int duracion, boolean like) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
        this.like = like;
    }

    public Cancion(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return "Cancion{" +
                "nombre='" + nombre + '\'' +
                ", duracion=" + duracion +
                ", id=" + id +
                ", like=" + like +
                '}';
    }
}
