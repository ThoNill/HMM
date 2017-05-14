package thomas.nill.hmm.modell;

import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Viterbi {
    private static Logger LOG = LogManager.getLogger(Viterbi.class);
    private HMM model;

    public Viterbi(HMM model) {
        super();
        this.model = model;
    }

    public int[] analyse(int[] emissionen) {
        ViterbiWert[][] viterbiWerte = start(emissionen);

        emmissionenDurchlaufen(emissionen, viterbiWerte);
        for (int i = 0; i < viterbiWerte.length; i++) {
            LOG.debug("Werte[" + i + "]"
                    + Arrays.toString(viterbiWerte[i]));
        }
        return zurücklaufen(emissionen, viterbiWerte);
    }

    protected ViterbiWert[][] start(int[] emissionen) {
        ViterbiWert[][] viterbiWerte = new ViterbiWert[emissionen.length + 1][model
                .getAnzahlDerZustände()];
        for (int zustand = 0; zustand < model.getAnzahlDerZustände(); zustand++) {
            viterbiWerte[0][zustand] = new ViterbiWert(1.0, zustand);
        }
        LOG.debug("Start:" + Arrays.toString(viterbiWerte[0]));
        return viterbiWerte;
    }

    protected void emmissionenDurchlaufen(int[] emissionen,
            ViterbiWert[][] viterbiWerte) {
        LOG.debug("Emissionen: " + Arrays.toString(emissionen));
        for (int i = 0; i < emissionen.length; i++) {
            int emission = emissionen[i];
            LOG.debug("Emission[" + i + "] = " + emissionen[i]);
            for (int zustand = 0; zustand < model.getAnzahlDerZustände(); zustand++) {
                viterbiWerte[i + 1][zustand] = viterbiSchritt(viterbiWerte[i],
                        zustand, emission, i);
            }
            // normieren(viterbiWerte[i]);
        }
    }

    private ViterbiWert viterbiSchritt(ViterbiWert[] viterbiWerte, int zustand,
            int emission, int index) {

        LOG.debug("Viterbi " + viterbiWerte[zustand] + " Zustand "
                + zustand);

        double max = 0.0;
        int maxZustand = 0;
        for (int nz = 0; nz < model.getAnzahlDerZustände(); nz++) {
            double h = model.getÜbergang(nz, zustand);
            if (index == 0) {
                LOG.debug("Index ist null");
                h = model.getStartHäufigkeit(zustand);
            }
            double wert = viterbiWerte[nz].wert * h;
            LOG.debug("Viterbi vorher " + viterbiWerte[nz].wert
                    + "\nÜbergang nach " + nz
                    + "\nÜbergangswahrscheinlichkeit " + h + " = " + wert);
            if (max < wert) {
                maxZustand = nz;
                max = wert;
            }
        }
        double emissionsHäufigkeit = model.getEmissionsHäufigkeit(zustand,emission);
        LOG.debug("Maximum " + max + " bei Übergang zu  " + maxZustand
                + "\neh= " + emissionsHäufigkeit + "\nViterbi= " + max
                * emissionsHäufigkeit);
        return new ViterbiWert(emissionsHäufigkeit * max, maxZustand);

    }

    private void normieren(ViterbiWert[] viterbiWerte) {
        double summe = summe(viterbiWerte);
        normieren(viterbiWerte, summe);
    }

    private double summe(ViterbiWert[] viterbiWerte) {
        double summe = 0.0;
        for (ViterbiWert w : viterbiWerte) {
            summe += w.wert;
        }
        return summe;
    }

    // [1, 1, 0, 0, 0, 1, 1, 0, 0, 0];
    // [0, 0, 1, 1, 0, 0, 0, 1, 1, 1];

    void normieren(ViterbiWert[] viterbiWerte, double summe) {
        LOG.debug("Summe: " + summe);
        LOG.debug("nicht Normiert: " + Arrays.toString(viterbiWerte));
        for (int i = 0; i < viterbiWerte.length; i++) {
            viterbiWerte[i].wert = viterbiWerte[i].wert / summe;
        }
        LOG.debug("Normiert: " + Arrays.toString(viterbiWerte));
    }

    private int[] zurücklaufen(int[] emissionen, ViterbiWert[][] viterbiWerte) {
        int[] zustände = new int[emissionen.length];
        zustände[emissionen.length - 1] = starteDasZurücklaufen(viterbiWerte[emissionen.length]);
        for (int zi = emissionen.length - 1; zi > 0; zi--) {
            zustände[zi - 1] = viterbiWerte[zi + 1][zustände[zi]].index;
        }
        return zustände;
    }

    private int starteDasZurücklaufen(ViterbiWert[] viterbiWerte) {
        int maxZustand = 0;
        double max = 0.0;
        for (int zustand = 0; zustand < model.getAnzahlDerZustände(); zustand++) {
            double wert = viterbiWerte[zustand].wert;
            LOG.debug("Starte Traceback wert " + wert + " in Zustand "
                    + zustand);
            if (max < wert) {
                maxZustand = zustand;
                max = wert;
            }
        }
        LOG.debug("Starte Traceback mit " + maxZustand);
        return maxZustand;
    }

}

class ViterbiWert {
    double wert;
    int index;

    public ViterbiWert(double wert, int index) {
        super();
        this.wert = wert;
        this.index = index;
    }

    @Override
    public String toString() {
        return "ViterbiWert [wert=" + wert + ", index=" + index + "]";
    }
}
