package com.example.fcmusicapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
    private String usuario;
    private String correo;
    private String contrasenia;
    private int id;

    public Usuario(String login,String correo, String password) {
        super();
        this.usuario = login;
        this.correo = correo;
        this.contrasenia = password;
    }
    public Usuario(Parcel source) {
        this.usuario = source.readString();
        this.contrasenia = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(usuario);
        dest.writeString(contrasenia);
    }
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }
        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getUsuario() {
        return usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
