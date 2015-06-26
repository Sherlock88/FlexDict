package in.dipanjan.flexdict;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Word {
    int ID, GREEDgeID, partsOfSpeech;
    Bitmap imgVisual;
    String strWord, strDefinition, strUsage, strSynonym, strAntonym;


    public Word() { }


    public Word(int ID, int GREEdgeID, String strWord, String strDefinition, String strUsage,
                int partsOfSpeech, String strSynonym, String strAntonym, byte[] imgVisual) {
        this.ID = ID;
        this.GREEDgeID = GREEdgeID;
        this.strWord = strWord;
        this.strDefinition = strDefinition;
        this.strUsage = strUsage;
        this.partsOfSpeech = partsOfSpeech;
        this.strSynonym = strSynonym;
        this.strAntonym = strAntonym;
        this.imgVisual = BitmapFactory.decodeByteArray(imgVisual, 0, imgVisual.length);
    }


    public Bitmap getVisual() {
        return imgVisual;
    }


    public void setVisual(byte[] imgVisual) {
        this.imgVisual = BitmapFactory.decodeByteArray(imgVisual, 0, imgVisual.length);
    }
}


