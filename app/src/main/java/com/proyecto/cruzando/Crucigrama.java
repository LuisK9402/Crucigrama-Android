package com.example.myapplication2;

public class Crucigrama {

    private Palabra[] palabras;
    private int wordCount; // Max quantity of words

    private int last; // Quantity of words stored

    public Crucigrama(int wordCount){
        this.palabras = new Palabra[wordCount];
        this.wordCount = wordCount;
        this.last = 0;
    }

    public void insertPalabra(Palabra palabra) {
        if(this.last<this.wordCount){
            this.palabras[last] = palabra;
        }
        this.last++;
    }

    // Returns the word at a given col, row and orientation
    public Palabra palabraAt(int col, int row, int orient){

        int row_p,col_p,count=0;

        for(int i=0; i<last; i++){
            if(palabras[i].getOrientacion()==orient){
                row_p = palabras[i].getHeadRow();
                col_p = palabras[i].getHeadColumn();
                count = 0;
                while(count<palabras[i].getLength()){
                    if(row_p==row && col_p==col){
                        return palabras[i];
                    }
                    count++;
                    if(orient==0){
                        col_p = palabras[i].getHeadColumn() + count;
                    }
                    else {
                        row_p = palabras[i].getHeadRow() + count;
                    }
                }
            }
        }
        return null;
    }

    // Returns the word at a given index
    public Palabra palabraAt(int idx){
        return this.palabras[idx];
    }

    public void setPalabras(Palabra[] palabras) {
        this.palabras = palabras;
        this.last = palabras.length;
    }

    public Palabra[] getPalabras() {
        return palabras;
    }

    public int getLast() {
        return last;
    }
}
