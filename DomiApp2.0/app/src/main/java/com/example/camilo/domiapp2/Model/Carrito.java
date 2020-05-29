package com.example.camilo.domiapp2.Model;

public class Carrito
{
    private String pid, NombreProducto, Precio, Cantidad, Descuento;

    public Carrito() {
    }

    public Carrito(String pid, String nombreProducto, String precio, String cantidad, String descuento) {
        this.pid = pid;
        NombreProducto = nombreProducto;
        Precio = precio;
        Cantidad = cantidad;
        Descuento = descuento;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        NombreProducto = nombreProducto;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }
}
