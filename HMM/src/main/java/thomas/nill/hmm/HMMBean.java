package thomas.nill.hmm;

import java.io.Serializable;

import org.janus.table.DefaultExtendedTableModel;

import thomas.nill.hmm.modell.HMM;
import thomas.nill.hmm.modell.MatrixHMM;

public class HMMBean implements Serializable {
    private HMM hmm;
    int anzahlZust�nde = 2;
    int anzahlEmissionen = 6;
    String[] zustandsNamen;
    String[] emissionsNamen;
    DefaultExtendedTableModel tm;
    private String erzeugteSequenz = " a b c d";

    public HMMBean() {
        super();
        create();
    }

    public int getAnzahlZust�nde() {
        return anzahlZust�nde;
    }

    public int getAnzahlEmissionen() {
        return anzahlEmissionen;
    }

    public void setAnzahlZust�nde(int anzahlZust�nde) {
        this.anzahlZust�nde = anzahlZust�nde;
    }

    public void setAnzahlEmissionen(int anzahlEmissionen) {
        this.anzahlEmissionen = anzahlEmissionen;
    }

    public void create() {
        zustandsNamen = new String[anzahlZust�nde];
        for (int i = 0; i < anzahlZust�nde; i++) {
            zustandsNamen[i] = "Z" + i;
        }
        emissionsNamen = new String[anzahlEmissionen];
        for (int i = 0; i < anzahlEmissionen; i++) {
            emissionsNamen[i] = "E" + i;
        }

        hmm = new MatrixHMM(zustandsNamen, emissionsNamen);

        hmm.setStartH�ufigkeit(0, 0.5);
        hmm.setStartH�ufigkeit(1, 0.5);

        hmm.set�bergang(0, 0, 0.8);
        hmm.set�bergang(0, 1, 0.2);
        hmm.set�bergang(1, 0, 0.2);
        hmm.set�bergang(1, 1, 0.8);

        for (int i = 0; i < 6; i++) {
            hmm.setEmissionsH�ufigkeit(0,i, 1.0 / 6.0);
            hmm.setEmissionsH�ufigkeit(1,i, 0.1);
        }
        hmm.setEmissionsH�ufigkeit(1, 5, 0.5);
        tm = new DefaultExtendedTableModel(zustandsNamen, zustandsNamen.length);
        for (int row = 0; row < zustandsNamen.length; row++) {
            for (int col = 0; col < zustandsNamen.length; col++) {
                tm.setValueAt(hmm.get�bergang(row, col), row, col);
            }
        }
        // tm.setRowCount(zustandsNamen.length);

    }

    public DefaultExtendedTableModel getUebergaenge() {
        return tm;
    }

    public String erzeugteSequenz() {
        return erzeugteSequenz;
    }

}
