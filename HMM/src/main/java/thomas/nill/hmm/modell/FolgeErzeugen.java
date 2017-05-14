package thomas.nill.hmm.modell;

import java.io.IOException;
import java.io.Writer;

public class FolgeErzeugen {
    private Zufall zufall;
    private HMM model;

    public FolgeErzeugen(HMM model, Zufall zufall) {
        super();
        this.model = model;
        this.zufall = zufall;
    }

    public FolgeErzeugen(HMM hmm) {
        this(hmm, new JavaZufall());
    }

    public HMMSequenceItem[] generate(int length) {
        HMMSequenceItem[] erg = new HMMSequenceItem[length];
        int aktuellerZustand = zufall.zufälligerIndex(model
                .getStartHäufigkeiten());

        for (int i = 0; i < erg.length; i++) {
            erg[i] = new HMMSequenceItem(zufall.zufälligerIndex(model
                    .emissionsHäufigkeiten(aktuellerZustand)), aktuellerZustand);
            aktuellerZustand = zufall.zufälligerIndex(model
                    .folgezustandsHäufigkeiten(aktuellerZustand));
        }
        return erg;
    }

    public String[] translate(HMMSequenceItem[] indexe) {
        String[] erg = new String[indexe.length];
        for (int i = 0; i < erg.length; i++) {
            erg[i] = model.getEmissionsName(indexe[i].emission);
        }
        return erg;
    }

    public void write(Writer writer, String trenner, HMMSequenceItem[] indexe)
            throws IOException {
        String[] erg = new String[indexe.length];
        for (int i = 0; i < erg.length; i++) {
            if (i > 0) {
                writer.append(trenner);
            }
            writer.append(model.getEmissionsName(indexe[i].emission));
        }
    }

    public void write(Writer writer, String trenner, int length)
            throws IOException {
        HMMSequenceItem[] indexe = generate(length);
        write(writer, trenner, indexe);
    }

    public void write(Writer writer, int length) throws IOException {
        write(writer, " ", length);
    }
}
