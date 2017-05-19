package thomas.nill.hmm;

import java.io.Serializable;

import org.janus.table.DefaultExtendedTableModel;

import thomas.nill.hmm.modell.HMM;
import thomas.nill.hmm.modell.MatrixHMM;

public class HMMBean implements Serializable {
    private HMM hmm;
    int anzahlZustände = 2;
    int anzahlEmissionen = 6;
    String[] zustandsNamen;
    String[] emissionsNamen;
    DefaultExtendedTableModel tm;
    private String erzeugteSequenz = " a b c d";

    public HMMBean() {
        super();
        create();
    }

    public int getAnzahlZustände() {
        return anzahlZustände;
    }

    public int getAnzahlEmissionen() {
        return anzahlEmissionen;
    }

    public void setAnzahlZustände(int anzahlZustände) {
        this.anzahlZustände = anzahlZustände;
    }

    public void setAnzahlEmissionen(int anzahlEmissionen) {
        this.anzahlEmissionen = anzahlEmissionen;
    }

    public void create() {
        zustandsNamen = new String[anzahlZustände];
        for (int i = 0; i < anzahlZustände; i++) {
            zustandsNamen[i] = "Z" + i;
        }
        emissionsNamen = new String[anzahlEmissionen];
        for (int i = 0; i < anzahlEmissionen; i++) {
            emissionsNamen[i] = "E" + i;
        }

        hmm = new MatrixHMM(zustandsNamen, emissionsNamen);

        hmm.setStartHäufigkeit(0, 0.5);
        hmm.setStartHäufigkeit(1, 0.5);

        hmm.setÜbergang(0, 0, 0.8);
        hmm.setÜbergang(0, 1, 0.2);
        hmm.setÜbergang(1, 0, 0.2);
        hmm.setÜbergang(1, 1, 0.8);

        for (int i = 0; i < 6; i++) {
            hmm.setEmissionsHäufigkeit(0,i, 1.0 / 6.0);
            hmm.setEmissionsHäufigkeit(1,i, 0.1);
        }
        hmm.setEmissionsHäufigkeit(1, 5, 0.5);
        tm = new DefaultExtendedTableModel(zustandsNamen, zustandsNamen.length);
        for (int row = 0; row < zustandsNamen.length; row++) {
            for (int col = 0; col < zustandsNamen.length; col++) {
                tm.setValueAt(hmm.getÜbergang(row, col), row, col);
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
