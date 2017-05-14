package thomas.nill.hmm.modell;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MatrixHMM implements HMM {
    private static Logger LOG = LogManager.getLogger(MatrixHMM.class);
    
    
    private double[][] �berg�nge;
    private double[][] emissionen;
    private String[] zustandsNamen;
    private String[] emissionsNamen;
    private double[] startH�ufigkeiten;

    public MatrixHMM(String[] zustandsNamen, String[] emissionsNamen) {
        this.zustandsNamen = zustandsNamen;
        this.emissionsNamen = emissionsNamen;
        int anzahlZust�nde = zustandsNamen.length;
        int anzahlEmisionen = emissionsNamen.length;
        �berg�nge = new double[anzahlZust�nde][anzahlZust�nde];
        emissionen = new double[anzahlZust�nde][anzahlEmisionen];
        startH�ufigkeiten = new double[anzahlZust�nde];
    }

    @Override
    public int getAnzahlDerZust�nde() {
        return zustandsNamen.length;
    }

    @Override
    public int getAnzahlDerEmissionen() {
        return emissionsNamen.length;
    }

    @Override
    public double get�bergang(int vonZustand, int nachZustand) {
        double w = �berg�nge[vonZustand][nachZustand];
        LOG.debug("get " + w + " �bergang nach " + nachZustand
                + " von " + vonZustand);
        return w;
    }

    @Override
    public void set�bergang(int vonZustand, int nachZustand, double p) {
        �berg�nge[vonZustand][nachZustand] = p;
    }

    @Override
    public double getEmissionsH�ufigkeit(int zustand,int emission) {
        return emissionen[zustand][emission];
    }

    @Override
    public void setEmissionsH�ufigkeit(int zustand,int emission,  double p) {
        emissionen[zustand][emission] = p;

    }

    @Override
    public String getZustandsName(int zustand) {
        return zustandsNamen[zustand];
    }

    @Override
    public String getEmissionsName(int emission) {
        return emissionsNamen[emission];
    }

    @Override
    public double[] emissionsH�ufigkeiten(int zustand) {
        return emissionen[zustand];
    }

    @Override
    public double[] folgezustandsH�ufigkeiten(int zustand) {
        return �berg�nge[zustand];
    }

    @Override
    public double getStartH�ufigkeit(int zustand) {
        return startH�ufigkeiten[zustand];
    }

    @Override
    public void setStartH�ufigkeit(int zustand, double p) {
        startH�ufigkeiten[zustand] = p;
    }

    @Override
    public double[] getStartH�ufigkeiten() {
        return startH�ufigkeiten;
    }

}
