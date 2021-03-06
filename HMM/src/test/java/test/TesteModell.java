package test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import thomas.nill.hmm.modell.BaumWelch;
import thomas.nill.hmm.modell.FolgeErzeugen;
import thomas.nill.hmm.modell.HMM;
import thomas.nill.hmm.modell.HMMSequenceItem;
import thomas.nill.hmm.modell.MatrixHMM;
import thomas.nill.hmm.modell.Viterbi;

public class TesteModell {

    public TesteModell() {
    }

    @Test
    public void testen() {

        HMM hmm = new MatrixHMM(new String[] { "GEN", "REP" }, new String[] {
                "A", "C", "G", "T" });

        hmm.setStartHäufigkeit(0, 0.3);
        hmm.setStartHäufigkeit(1, 0.7);

        hmm.setÜbergang(0, 0, 0.8);
        hmm.setÜbergang(1, 0, 0.2);
        hmm.setÜbergang(0, 1, 0.2);
        hmm.setÜbergang(1, 1, 0.8);

        assertEquals(0.2, hmm.getÜbergang(0, 1), 0.01);
        assertEquals(0.2, hmm.getÜbergang(1, 0), 0.01);
        assertEquals(0.8, hmm.getÜbergang(0, 0), 0.01);
        assertEquals(0.8, hmm.getÜbergang(1, 1), 0.01);

        hmm.setEmissionsHäufigkeit(0, 0, 0.20);
        hmm.setEmissionsHäufigkeit(0, 1, 0.20);
        hmm.setEmissionsHäufigkeit(0, 2, 0.30);
        hmm.setEmissionsHäufigkeit(0, 3, 0.30);
        hmm.setEmissionsHäufigkeit(1, 0, 0.4);
        hmm.setEmissionsHäufigkeit(1, 1, 0.4);
        hmm.setEmissionsHäufigkeit(1, 2, 0.1);
        hmm.setEmissionsHäufigkeit(1, 3, 0.1);

        FolgeErzeugen erz = new FolgeErzeugen(hmm);
        try {
            erz.write(new Writer() {

                @Override
                public void write(char[] cbuf, int off, int len)
                        throws IOException {
                    for (int pos = off; pos < off + len; pos++) {
                        System.out.append(cbuf[pos]);
                    }
                }

                @Override
                public void flush() throws IOException {
                }

                @Override
                public void close() throws IOException {
                }
            }, 30);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void viterbi() {

        HMM hmm = new MatrixHMM(new String[] { "GEN", "REP" }, new String[] {
                "A", "C", "G", "T" });

        hmm.setStartHäufigkeit(0, 1.0);
        hmm.setStartHäufigkeit(1, 0.);

        hmm.setÜbergang(0, 0, 0.6);
        hmm.setÜbergang(1, 0, 0.4);
        hmm.setÜbergang(0, 1, 0.6);
        hmm.setÜbergang(1, 1, 0.4);

        hmm.setEmissionsHäufigkeit(0, 0, 0.45);
        hmm.setEmissionsHäufigkeit(0, 1, 0.45);
        hmm.setEmissionsHäufigkeit(0, 2, 0.05);
        hmm.setEmissionsHäufigkeit(0, 3, 0.05);

        hmm.setEmissionsHäufigkeit(1, 0, 0.05);
        hmm.setEmissionsHäufigkeit(1, 1, 0.05);
        hmm.setEmissionsHäufigkeit(1, 2, 0.45);
        hmm.setEmissionsHäufigkeit(1, 3, 0.45);

        FolgeErzeugen erz = new FolgeErzeugen(hmm);

        HMMSequenceItem[] werte = erz.generate(20);
        System.out.println();
        System.out.println(Arrays.toString(werte));

        Viterbi analyse = new Viterbi(hmm);

        System.out.println("Vermutet: "
                + Arrays.toString(analyse.analyse(HMMSequenceItem
                        .toEmissionen(werte))));
        System.out.println("Original: "
                + Arrays.toString(HMMSequenceItem.toZustände(werte)));

    }

    @Ignore
    @Test
    public void viterbi2() {

        HMM hmm = new MatrixHMM(new String[] { "F", "U" }, new String[] { "1",
                "2", "3", "4", "5", "6" });

        hmm.setStartHäufigkeit(0, 0.5);
        hmm.setStartHäufigkeit(1, 0.5);

        hmm.setÜbergang(0, 0, 0.8);
        hmm.setÜbergang(1, 0, 0.2);
        hmm.setÜbergang(0, 1, 0.2);
        hmm.setÜbergang(1, 1, 0.8);

        for (int i = 0; i < 6; i++) {
            hmm.setEmissionsHäufigkeit(0, i, 1.0 / 6.0);
            hmm.setEmissionsHäufigkeit(1, i, 0.1);
        }
        hmm.setEmissionsHäufigkeit(1, 5, 0.5);

        Viterbi analyse = new Viterbi(hmm);

        System.out.println("Vermutet: "
                + Arrays.toString(analyse.analyse(new int[] { 2, 0, 4, 5 })));
        System.out
                .println("Vermutet: "
                        + Arrays.toString(analyse.analyse(new int[] { 2, 0, 4,
                                5, 5 })));

    }

    @Test
    public void baumWelch() {

        HMM hmm = new MatrixHMM(new String[] { "F", "U" }, new String[] { "1",
                "2", "3", "4", "5", "6" });

        hmm.setStartHäufigkeit(0, 0.5);
        hmm.setStartHäufigkeit(1, 0.5);

        hmm.setÜbergang(0, 0, 0.8);
        hmm.setÜbergang(1, 0, 0.2);
        hmm.setÜbergang(0, 1, 0.2);
        hmm.setÜbergang(1, 1, 0.8);

        for (int i = 0; i < 6; i++) {
            hmm.setEmissionsHäufigkeit(0, i, 1.0 / 6.0);
            hmm.setEmissionsHäufigkeit(1, i, 0.1);
        }
        hmm.setEmissionsHäufigkeit(1, 5, 0.5);

        HMM hmmTrain = new MatrixHMM(new String[] { "F", "U" }, new String[] {
                "1", "2", "3", "4", "5", "6" });

        hmmTrain.setStartHäufigkeit(0, 0.5);
        hmmTrain.setStartHäufigkeit(1, 0.5);

        hmmTrain.setÜbergang(0, 0, 0.4);
        hmmTrain.setÜbergang(1, 0, 0.6);
        hmmTrain.setÜbergang(0, 1, 0.6);
        hmmTrain.setÜbergang(1, 1, 0.4);

        for (int i = 0; i < 6; i++) {
            hmmTrain.setEmissionsHäufigkeit(0, i, 1.0 / 6.0);
            hmmTrain.setEmissionsHäufigkeit(1, i, 0.1);
        }
        hmmTrain.setEmissionsHäufigkeit(1, 5, 0.5);

        FolgeErzeugen erz = new FolgeErzeugen(hmm);
        prüfen(hmm);
        prüfen(hmmTrain);

        BaumWelch trainer = new BaumWelch(hmmTrain);

        for (int i = 0; i < 1; i++) {
            HMMSequenceItem[] werte = erz.generate(1000);
            int[] emissionen = HMMSequenceItem.toEmissionen(werte);
            trainer.updateHmm(emissionen);
        }
        ausgabe(hmm);
        ausgabe(hmmTrain);
        prüfen(hmmTrain);

    }

    private void prüfen(HMM hmm) {
        if (!hmm.testeWerte()) {
            throw new IllegalStateException("keine konsistenten Werte");
        }

    }

    private void ausgabe(HMM model) {
        System.out.println();
        for (int i = 0; i < model.getAnzahlDerZustände(); i++) {
            System.out.println();
            for (int j = 0; j < model.getAnzahlDerZustände(); j++) {
                System.out.print("hmm[" + i + "," + j + "]= "
                        + model.getÜbergang(i, j) + " ");
            }
        }
        System.out.println();
    }

}
