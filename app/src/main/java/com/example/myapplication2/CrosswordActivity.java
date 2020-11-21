package com.example.myapplication2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class CrosswordActivity extends AppCompatActivity {

    GridLayout target; // CW's GridLayout
    int[][] mtxId; // Matrix that contains each CharField's Id by (row, col)
    char[][] mtxChr; // Matrix that contains each character that should go in each (row,col) [CW Solution]
    int mtxSize; // Number of rows/columns of the whole matrix, including inactive cells
    int wordCount; // Number of words in the CW
    private int cellSize; // Height/Width of each cell
    FocusInfo focusInfo; // Contains info about focused cells
    Crucigrama crucigrama; // Object that contains all the words of current CW
    TextToSpeech t2v; // Text to speech
    boolean txt2voiceActive;
    DatabaseAcces db;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);
        TextView pista = findViewById(R.id.pista);
        Intent intent = getIntent();
        int level = intent.getIntExtra("level",0);
        db = DatabaseAcces.getInstance(this);

//        Toast.makeText(this, level, Toast.LENGTH_SHORT).show();

        SharedPreferences preferencias = getSharedPreferences("cw_preferences", Context.MODE_PRIVATE);

        txt2voiceActive= preferencias.getBoolean("txt2voice",false);

        t2v = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = t2v.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Lenguaje no soportado");
                    }
                } else {
                    Log.e("TTS", "Fallo en inicialización de TTS");
                }
            }
        });

        target = findViewById(R.id.myGrid);
        cellSize = evenWidth(target);



        mtxSize = 11;
        mtxChr = new char[mtxSize][mtxSize];
        mtxId = new int[mtxSize][mtxSize];

        focusInfo = new FocusInfo();
        focusInfo.setFocusedOrientation(0);

        //Prueba
//        final Palabra palabra1 = new Palabra(0, "CASA",
//                "Lugar donde viven las personas", 1, 4, 1);
//        final Palabra palabra2 = new Palabra(1, "PANAL",
//                "Lugar donde viven las abejas", 2, 1, 0);
//        final Palabra palabra3 = new Palabra(2, "CAS",
//                "Fruta verde que se usa para hacer fresco y helados", 4, 3, 0);
//        final Palabra palabra4 = new Palabra(3, "APIO",
//                "Verdura larga, crujiente y verde, de bajas calorias", 1, 1, 1);
//
//        wordCount = 4;
//        crucigrama = new Crucigrama(wordCount);
//        crucigrama.insertPalabra(palabra1);
//        crucigrama.insertPalabra(palabra2);
//        crucigrama.insertPalabra(palabra3);
//        crucigrama.insertPalabra(palabra4);

        db.openDb();
        String json_string = db.getCwData(level);
        db.closeDb();

        try{
            JSONObject jsonObject = new JSONObject(json_string);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            wordCount = jsonArray.length();
            crucigrama = new Crucigrama(wordCount);
            for(int i=0; i<jsonArray.length();i++){
                JSONObject dato = jsonArray.getJSONObject(i);
                //Prueba
                String pal = dato.getString("palabra");
                String desc = dato.getString("descripcion");
                int hrow = dato.getInt("headRow");
                int hcol = dato.getInt("headCol");
                int hori = dato.getInt("orientacion");
                Palabra nuevaPalabra = new Palabra(i,dato.getString("palabra"),dato.getString("descripcion"),dato.getInt("headRow"),dato.getInt("headCol"),dato.getInt("orientacion"));
                this.crucigrama.insertPalabra(nuevaPalabra);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        pista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = ((TextView) v).getText().toString();
                read(texto);
            }
        });

        // All this happens once the Grid size is already set
        target.post(new Runnable() {
            @Override
            public void run() {
                setCellSize();
                fillBlank();
                for (int i = 0; i < crucigrama.getLast(); i++) {
                    placeWord(crucigrama.palabraAt(i));
                }
                activateHeadsCells();
                setWordFocus(mtxId[crucigrama.palabraAt(0).getHeadRow()][crucigrama.palabraAt(0).getHeadColumn()]);
                createKeyboard();
            }
        });


    }

    //////////////////////////////////////////////////////////////////////////

    private void read(String texto){
        if(this.txt2voiceActive){
            this.t2v.speak(texto, TextToSpeech.QUEUE_ADD, null);
        }
    }

    // Sets the cell size
    private void setCellSize() {
        this.cellSize = evenWidth(this.target);
    }


    // Sets a cell active (BG color, clickable, assigns listeners)
    public void activateCell(int fila, int columna) {

        CharField myEdtxt = findViewById(mtxId[fila][columna]);
        myEdtxt.setFocusable(true);
        myEdtxt.setText("");
        myEdtxt.setFocusableInTouchMode(true);
        myEdtxt.setClickable(true);
        myEdtxt.setBackground(ContextCompat.getDrawable(this, R.drawable.fondoh_lvl_0));
        myEdtxt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(focusInfo.getCharFocusedId()!=-1) {
                        removeWordFocus(focusInfo.getCharFocusedId());
                    }
                    setWordFocus(v.getId());
                } else {
                    removeWordFocus(v.getId());
                }
            }
        });

        myEdtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isFocused()) {

                    removeWordFocus(v.getId());
                    switchOrientation();
                    setWordFocus(v.getId());
                }
            }
        });

    }

    public void activateHeadsCells(){
        for(int i=0; i<crucigrama.getLast(); i++){
            Palabra palabra = crucigrama.palabraAt(i);
            int headRow = palabra.getHeadRow();
            int headCol = palabra.getHeadColumn();
            int idx = palabra.getIdx()+1;
            setHeadFocusColor(headRow,headCol,0,idx);
        }
    }

    // Change color of cell acordding to focus level)
    public void setFocusColor(int fila, int columna, int focus_lvl) {
        CharField myEdtxt = findViewById(mtxId[fila][columna]);
        switch (focus_lvl) {
            case 0:
                myEdtxt.setBackground(ContextCompat.getDrawable(this, R.drawable.fondoh_lvl_0));
                break;
            case 1:
                myEdtxt.setBackground(ContextCompat.getDrawable(this, R.drawable.fondoh_lvl_1));
                break;
            case 2:
                myEdtxt.setBackground(ContextCompat.getDrawable(this, R.drawable.fondoh_lvl_2));
                break;
        }
//        myEdtxt.setWidth(this.cellSize);
//        myEdtxt.setHeight(this.cellSize);

    }

    public void setHeadFocusColor(int fila, int columna, int focus_lvl, int idx){
        CharField myEdtxt = findViewById(mtxId[fila][columna]);
        String resName = "fondoh_lvl_";
        resName = resName + String.valueOf(focus_lvl) + "_" + String.valueOf(idx);

        int resourceID = getResources().getIdentifier(
                resName,
                "drawable",
                getPackageName()
        );
        myEdtxt.setBackground(ContextCompat.getDrawable(this, resourceID));
    }

    // Inverts the focus orientation
    public void switchOrientation() {
        if (this.focusInfo.getFocusedOrientation() == Palabra.HORIZONTAL) {
            this.focusInfo.setFocusedOrientation(Palabra.VERTICAL);
        } else {
            this.focusInfo.setFocusedOrientation(Palabra.HORIZONTAL);
        }
//        Toast.makeText(this, String.valueOf(this.focusedOrientation), Toast.LENGTH_SHORT).show();
    }

    // Sets the respective focus of a Palabra and CharField given a CharField id
    public void setWordFocus(int id) {
        CharField edTxt = findViewById(id);
        this.focusInfo.setCharFocusedId(id);
        Palabra palabra = crucigrama.palabraAt(edTxt.getCol(), edTxt.getRow(), this.focusInfo.getFocusedOrientation());
        if (palabra == null) {
            switchOrientation();
            palabra = crucigrama.palabraAt(edTxt.getCol(), edTxt.getRow(), this.focusInfo.getFocusedOrientation());
        }
        if (palabra != null) {
            TextView pista = findViewById(R.id.pista);
            pista.setText(palabra.getDescripcion());
            int head_row = palabra.getHeadRow();
            int head_col = palabra.getHeadColumn();
            int col = head_col;
            int row = head_row;
            int i = 0;
            this.focusInfo.setWordFocusedId(palabra.getIdx());
            while (i < palabra.getLength()) {
                if (this.focusInfo.getFocusedOrientation() == Palabra.HORIZONTAL) {
                    col = head_col + i;
                } else {
                    row = head_row + i;
                }
                if(!((CharField)findViewById(mtxId[row][col])).isCorrect()) {
                    if(row == head_row && col == head_col){
                        setHeadFocusColor(head_row,head_col,1,palabra.getIdx()+1);
                    }else {
                        setFocusColor(row, col, 1);
                    }
                }
                i++;
            }
            if(edTxt.getRow() == head_row && edTxt.getCol() == head_col){
                setHeadFocusColor(head_row,head_col,2,palabra.getIdx()+1);
            }else {
                setFocusColor(edTxt.getRow(), edTxt.getCol(), 2);
            }

        } else {
            switchOrientation();
//            Toast.makeText(this, "ERROR 1", Toast.LENGTH_SHORT).show();
        }


    }

    // Removes word focus
    public void removeWordFocus(int id) {
        CharField edTxt = findViewById(id);
        Palabra palabra = crucigrama.palabraAt(edTxt.getCol(), edTxt.getRow(), this.focusInfo.getFocusedOrientation());
        if (palabra != null) {
            int head_row = palabra.getHeadRow();
            int head_col = palabra.getHeadColumn();
            int col = head_col;
            int row = head_row;
            int i = 0;
            while (i < palabra.getLength()) {
                if (this.focusInfo.getFocusedOrientation() == Palabra.HORIZONTAL) {
                    col = head_col + i;
                } else {
                    row = head_row + i;
                }
                if(!((CharField)findViewById(mtxId[row][col])).isCorrect()) {
                    setFocusColor(row, col, 0);
                }
                i++;
            }
        }
    }

    // Sets the chars  of a word in the corresponding (row,col) of the solution matrix
    public void setOnCharMtx(int fila, int columna, Palabra palabra, int idx) {
//        Toast.makeText(this, String.valueOf(idx), Toast.LENGTH_SHORT).show();
        this.mtxChr[fila][columna] = palabra.getPalabra().charAt(idx);
    }

    // Sets a word in its corresponding cell in the grid
    public void placeWord(Palabra palabra) {
        int fila_h = palabra.getHeadRow();
        int columna_h = palabra.getHeadColumn();
        int idx = 0;
        int fila = fila_h;
        int columna = columna_h;
//        char letra;


        linkWord(palabra);
//        Toast.makeText(this, String.valueOf(palabra.getLength()), Toast.LENGTH_SHORT).show();

        while (idx < palabra.getLength()) {

//            letra = palabra.getPalabra().charAt(idx);
            setOnCharMtx(fila, columna, palabra, idx);
            activateCell(fila, columna);

            idx++;
            if (palabra.getOrientacion() == Palabra.VERTICAL) {
                fila = fila_h + idx;
            } else {
                columna = columna_h + idx;
            }


        }
    }

    // Calculates the cell size, to distribute evenly according to parent width and the matrix size
    public int evenWidth(GridLayout gl) {
//        Toast.makeText(this, String.valueOf(gl.getWidth()), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, String.valueOf(gl.getColumnCount()), Toast.LENGTH_SHORT).show();

        return gl.getWidth() / gl.getColumnCount();
    }

    // Initializes all cells blank and inactive
    public void fillBlank() {

        int newId;
        for (int i = 0; i < this.mtxSize; i++) {
            for (int j = 0; j < this.mtxSize; j++) {

                CharField myTxt = new CharField(this);
                GridLayout.LayoutParams nlparams = new GridLayout.LayoutParams(
                        GridLayout.spec(i),
                        GridLayout.spec(j));

                nlparams.setMargins(1, 1, 1, 1);
                nlparams.setGravity(Gravity.CENTER);
                newId = ViewCompat.generateViewId();
                myTxt.setId(newId);
                this.mtxId[i][j] = newId;
                myTxt.setPos(i, j);
                myTxt.setLayoutParams(nlparams);
                myTxt.setBackground(null);
                myTxt.setText("");
                myTxt.setWidth(this.cellSize);
                myTxt.setHeight(this.cellSize);
                myTxt.setGravity(Gravity.CENTER);
                myTxt.setFocusable(false);
                myTxt.setCursorVisible(false);

                target.addView(myTxt);

            }
        }
    }

    // Assigns a word to its corresponding cell
    public void linkWord(Palabra palabra) {
        int row_h = palabra.getHeadRow();
        int col_h = palabra.getHeadColumn();
        int col = col_h;
        int row = row_h;
        CharField charAux;
        for (int i = 0; i < palabra.getLength(); i++) {
            if (palabra.getOrientacion() == Palabra.HORIZONTAL) {
                col = col_h + i;
            } else {
                row = row_h + i;
            }
            charAux = findViewById(mtxId[row][col]);
            charAux.setLinkedWordId(palabra.getIdx());
        }

    }


    //////////////////////////////////////////////////
    //  KEYBOARD
    //////////////////////////////////////////////////

    // Creates the keyboard layout
    public void createKeyboard() {

        LinearLayout keyboardLine;

        int[] lineas = {R.id.linea1,R.id.linea2,R.id.linea3};

        int maxCol = 10;
//        int maxRow = 3;

        String[] abc = new String[]{"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                "A", "S", "D", "F", "G", "H", "J", "K", "L", "Ñ",
                "Z", "X", "C", "V", "B", "N", "M", "←", "←", "←"};

        for (int i=0; i<lineas.length; i++){
            keyboardLine = findViewById(lineas[i]);
            for (int j=0; j<maxCol;j++) {
                Button myBtn = new Button(getApplicationContext());
                LinearLayout.LayoutParams nlparams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                myBtn.setText(abc[i*maxCol+j]);
                myBtn.setLayoutParams(nlparams);
                myBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLetter(v);
                    }
                });
                keyboardLine.addView(myBtn);
            }
        }
    }

    // Changes the text of the focused CharField according to the pressed button
    // And checks if word is correct and if there is any unfinished word left or game is over
    public void setLetter(View v){
        int inc_dec;
        String letter = ((Button)v).getText().toString();
        CharField myEdt = findViewById(focusInfo.getCharFocusedId());
        CharField myEdtAux;
        String previousText = (String) myEdt.getText();
        if(letter=="←"){
            myEdt.setText("");
        }
        else{
            myEdt.setText(letter);
        }

        read(letter);
        Palabra palabra;
        Palabra correctWordId;
        if(isCorrect(this.crucigrama.getPalabras()[this.focusInfo.getWordFocusedId()])){
            palabra = findUnfinishedWord();
            if(palabra==null){
                // TODO Win function
                setCorrect(this.crucigrama.getPalabras()[this.focusInfo.getWordFocusedId()]);
                Toast.makeText(this, "Ganó !!!", Toast.LENGTH_SHORT).show();
            }
            else{
                removeWordFocus(myEdt.getId());
                correctWordId = this.crucigrama.getPalabras()[this.focusInfo.getWordFocusedId()];
                int emptyChar = firstEmpty(palabra);
                focusInfo.setFocusInfo(emptyChar, palabra.getOrientacion(), palabra.getIdx());
                CharField nextEdt = findViewById(emptyChar);
                nextEdt.requestFocus();
                setCorrect(correctWordId);
                //setWordFocus(emptyChar);

                return;
            }
        }
        if(letter.equals("←")){
            read("Borrar");
            inc_dec=-1;
            if(previousText.equals("")){
                if(nextFocus(myEdt.getId(),inc_dec)) { // Si no es el primero
                    myEdtAux = findViewById(focusInfo.getCharFocusedId());
                    myEdtAux.setText("");
                }
            }
        }
        else {
            inc_dec=1;
            nextFocus(myEdt.getId(),inc_dec);
        }


    }

    // Checks if a word is complete and sets the complete bit in true
    public boolean isCorrect(Palabra palabra){
        CharField myEdtxt;
        int row=palabra.getHeadRow();
        int col=palabra.getHeadColumn();
        int idx=0;
//        Palabra palabraAux;
//        int[] ids;

        while (idx < palabra.getLength()) {

            myEdtxt = findViewById(mtxId[row][col]);
            if(!myEdtxt.getText().equals(String.valueOf(mtxChr[row][col]))){
                return false;
            }

            idx++;
            if (palabra.getOrientacion() == Palabra.VERTICAL) {
                row = palabra.getHeadRow() + idx;
            } else {
                col = palabra.getHeadColumn() + idx;
            }
        }
/*        myEdtxt = findViewById(this.focusInfo.getCharFocusedId());
        for(int i=0; i<myEdtxt.getLinkedWordsId().length;i++){
            ids = myEdtxt.getLinkedWordsId();
            palabraAux = this.crucigrama.getPalabras()[ids[i]];
            palabraAux.setReady(true);
        }

 */
        palabra.setReady(true);
        return true;

    }

    // Finds a word thas isn't complete
    public Palabra findUnfinishedWord(){
        for(int i=0;i<this.crucigrama.getLast();i++){
            if(!crucigrama.getPalabras()[i].isReady()){
                return crucigrama.getPalabras()[i];
            }
        }
        return null;
    }

    // Changes the focus to the next/previous CharField after a key is pressed
    public boolean nextFocus(int id, int inc_dec){
        int next_row;
        int next_col;
        int next_id;
        int max_row;
        int max_col;
        CharField myEdtxt = findViewById(id);
        int col = myEdtxt.getCol();
        int row = myEdtxt.getRow();
        Palabra palabra = crucigrama.palabraAt(col,row,focusInfo.getFocusedOrientation());
        if(myEdtxt.getRow()<this.mtxSize && myEdtxt.getCol()<this.mtxSize) {
            if (focusInfo.getFocusedOrientation() == Palabra.HORIZONTAL) {
                next_row = myEdtxt.getRow();
                next_col = myEdtxt.getCol() + inc_dec;
                max_row = myEdtxt.getRow();
                max_col = palabra.getHeadColumn() + palabra.getLength() - inc_dec;
            } else {
                next_row = myEdtxt.getRow() + inc_dec;
                next_col = myEdtxt.getCol();
                max_row = palabra.getHeadRow() + palabra.getLength() - inc_dec;
                max_col = myEdtxt.getCol();
            }
            while (((CharField) findViewById(mtxId[next_row][next_col])).isCorrect()) {
                if (focusInfo.getFocusedOrientation() == Palabra.HORIZONTAL) {
                    next_col = next_col + inc_dec;
                } else {
                    next_row = next_row + inc_dec;
                }
            }
            if (next_row <= max_row && next_col <= max_col && next_row >= palabra.getHeadRow() && next_col >= palabra.getHeadColumn()) {
                next_id = mtxId[next_row][next_col];
                setWordFocus(next_id);
                return true;
            }
        }
        return false;

    }

    // Finds the first empty char in a word
    public int firstEmpty(Palabra palabra){
        CharField myEdtxt;
        int row=palabra.getHeadRow();
        int col=palabra.getHeadColumn();
        int idx=0;

        while (idx < palabra.getLength()) {

            myEdtxt = findViewById(mtxId[row][col]);
            if(myEdtxt.getText().equals("")){
                return mtxId[row][col];
            }

            idx++;
            if (palabra.getOrientacion() == Palabra.VERTICAL) {
                row = palabra.getHeadRow() + idx;
            } else {
                col = palabra.getHeadColumn() + idx;
            }
        }
        return -1;
    }

    // Changes color of a word that is correct
    public void setCorrect(Palabra palabra){
        CharField myEdtxt;
        int row=palabra.getHeadRow();
        int col=palabra.getHeadColumn();
        int idx=0;

        while (idx < palabra.getLength()) {

            myEdtxt = findViewById(mtxId[row][col]);
            myEdtxt.setBackground(ContextCompat.getDrawable(this, R.drawable.correct));
            myEdtxt.setCorrect(true);
            myEdtxt.setFocusable(false);
            myEdtxt.setCursorVisible(false);

            idx++;
            if (palabra.getOrientacion() == Palabra.VERTICAL) {
                row = palabra.getHeadRow() + idx;
            } else {
                col = palabra.getHeadColumn() + idx;
            }
        }
        read(palabra.getPalabra());
        read("Palabra Correcta!");
    }
}