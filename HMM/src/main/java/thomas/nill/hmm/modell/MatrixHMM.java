package thomas.nill.hmm.modell;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MatrixHMM implements HMM {
    private static Logger LOG = LogManager.getLogger(MatrixHMM.class);
    
    
    private double[][] übergänge;
    private double[][] emissionen;
    private String[] zustandsNamen;
    private String[] emissionsNamen;
    private double[] startHäufigkeiten;

    public MatrixHMM(String[] zustandsNamen, String[] emissionsNamen) {
        this.zustandsNamen = zustandsNamen;
        this.emissionsNamen = emissionsNamen;
        int anzahlZustände = zustandsNamen.length;
        int anzahlEmisionen = emissionsNamen.length;
        übergänge = new double[anzahlZustände][anzahlZustände];
        emissionen = new double[anzahlZustände][anzahlEmisionen];
        startHäufigkeiten = new double[anzahlZustände];
    }

    @Override
    public int getAnzahlDerZustände() {
        return zustandsNamen.length;
    }

    @Override
    public int getAnzahlDerEmissionen() {
        return emissionsNamen.length;
    }

    @Override
    public double getÜbergang(int vonZustand, int nachZustand) {
        double w = übergänge[vonZustand][nachZustand];
        LOG.debug("get " + w + " Übergang nach " + nachZustand
                + " von " + vonZustand);
        return w;
    }

    @Override
    public void setÜbergang(int vonZustand, int nachZustand, double p) {
        übergänge[vonZustand][nachZustand] = p;
    }

    @Override
    public double getEmissionsHäufigkeit(int zustand,int emission) {
        return emissionen[zustand][emission];
    }

    @Override
    public void setEmissionsHäufigkeit(int zustand,int emission,  double p) {
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
    public double[] emissionsHäufigkeiten(int zustand) {
        return emissionen[zustand];
    }

    @Override
    public double[] folgezustandsHäufigkeiten(int zustand) {
        return übergänge[zustand];
    }

    @Override
    public double getStartHäufigkeit(int zustand) {
        return startHäufigkeiten[zustand];
    }

    @Override
    public void setStartHäufigkeit(int zustand, double p) {
        startHäufigkeiten[zustand] = p;
    }

    @Override
    public double[] getStartHäufigkeiten() {
        return startHäufigkeiten;
    }

}
