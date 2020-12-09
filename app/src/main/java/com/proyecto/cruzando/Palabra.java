package com.proyecto.cruzando;

public class Palabra {

    private int idx;
    private String palabra;
    private String descripcion;
    private int[] head;
    private int orientacion;
    private int length;
    private boolean ready;
    final static int HORIZONTAL = 0;
    final static int VERTICAL = 1;

    public Palabra(int idx, String palabra, String descripcion, int headRow, int headColumn, int orientacion){
        this.idx = idx;
        this.palabra = palabra;
        this.descripcion = descripcion;
        this.head = new int[2];
        this.head[0] = headRow;
        this.head[1] = headColumn;
        this.orientacion = orientacion;
        this.length = palabra.length();
        this.ready = false;
    }


////// Getters ///////////////
    public int getIdx() {
        return idx;
    }


    public String getPalabra() {
        return palabra;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public int getOrientacion() {
        return orientacion;
    }


    public int getLength() {
        return length;
    }

    public int getHeadRow(){
        return this.head[0];
    }

    public int getHeadColumn(){
        return this.head[1];
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
