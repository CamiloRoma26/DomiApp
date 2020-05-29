package com.example.camilo.domiapp2.Model;

public class Usuarios
{
    private  String Nombre, Telefono, Contraseña, image, address;

    public Usuarios()
    {

    }

    public Usuarios(String Nombre, String Telefono, String Contraseña, String image, String address) {
        this.Nombre = Nombre;
        this.Telefono = Telefono;
        this.Contraseña = Contraseña;
        this.image = image;
        this.address = address;

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
