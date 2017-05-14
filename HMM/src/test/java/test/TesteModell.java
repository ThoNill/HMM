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

        hmm.setStartH‰ufigkeit(0, 0.3);
        hmm.setStartH‰ufigkeit(1, 0.7);

        hmm.set‹bergang(0, 0, 0.8);
        hmm.set‹bergang(1, 0, 0.2);
        hmm.set‹bergang(0, 1, 0.2);
        hmm.set‹bergang(1, 1, 0.8);

        assertEquals(0.2, hmm.get‹bergang(0, 1), 0.01);
        assertEquals(0.2, hmm.get‹bergang(1, 0), 0.01);
        assertEquals(0.8, hmm.get‹bergang(0, 0), 0.01);
        assertEquals(0.8, hmm.get‹bergang(1, 1), 0.01);

        hmm.setEmissionsH‰ufigkeit(0, 0, 0.20);
        hmm.setEmissionsH‰ufigkeit(0, 1, 0.20);
        hmm.setEmissionsH‰ufigkeit(0, 2, 0.30);
        hmm.setEmissionsH‰ufigkeit(0, 3, 0.30);
        hmm.setEmissionsH‰ufigkeit(1, 0, 0.4);
        hmm.setEmissionsH‰ufigkeit(1, 1, 0.4);
        hmm.setEmissionsH‰ufigkeit(1, 2, 0.1);
        hmm.setEmissionsH‰ufigkeit(1, 3, 0.1);

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

        hmm.setStartH‰ufigkeit(0, 1.0);
        hmm.setStartH‰ufigkeit(1, 0.);

        hmm.set‹bergang(0, 0, 0.6);
        hmm.set‹bergang(1, 0, 0.4);
        hmm.set‹bergang(0, 1, 0.6);
        hmm.set‹bergang(1, 1, 0.4);

        hmm.setEmissionsH‰ufigkeit(0, 0, 0.45);
        hmm.setEmissionsH‰ufigkeit(0, 1, 0.45);
        hmm.setEmissionsH‰ufigkeit(0, 2, 0.05);
        hmm.setEmissionsH‰ufigkeit(0, 3, 0.05);

        hmm.setEmissionsH‰ufigkeit(1, 0, 0.05);
        hmm.setEmissionsH‰ufigkeit(1, 1, 0.05);
        hmm.setEmissionsH‰ufigkeit(1, 2, 0.45);
        hmm.setEmissionsH‰ufigkeit(1, 3, 0.45);

        FolgeErzeugen erz = new FolgeErzeugen(hmm);

        HMMSequenceItem[] werte = erz.generate(20);
        System.out.println();
        System.out.println(Arrays.toString(werte));

        Viterbi analyse = new Viterbi(hmm);

        System.out.println("Vermutet: "
                + Arrays.toString(analyse.analyse(HMMSequenceItem
                        .toEmissionen(werte))));
        System.out.println("Original: "
                + Arrays.toString(HMMSequenceItem.toZust‰nde(werte)));

    }

    @Ignore
    @Test
    public void viterbi2() {

        HMM hmm = new MatrixHMM(new String[] { "F", "U" }, new String[] { "1",
                "2", "3", "4", "5", "6" });

        hmm.setStartH‰ufigkeit(0, 0.5);
        hmm.setStartH‰ufigkeit(1, 0.5);

        hmm.set‹bergang(0, 0, 0.8);
        hmm.set‹bergang(1, 0, 0.2);
        hmm.set‹bergang(0, 1, 0.2);
        hmm.set‹bergang(1, 1, 0.8);

        for (int i = 0; i < 6; i++) {
            hmm.setEmissionsH‰ufigkeit(0, i, 1.0 / 6.0);
            hmm.setEmissionsH‰ufigkeit(1, i, 0.1);
        }
        hmm.setEmissionsH‰ufigkeit(1, 5, 0.5);

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

        hmm.setStartH‰ufigkeit(0, 0.5);
        hmm.setStartH‰ufigkeit(1, 0.5);

        hmm.set‹bergang(0, 0, 0.8);
        hmm.set‹bergang(1, 0, 0.2);
        hmm.set‹bergang(0, 1, 0.2);
        hmm.set‹bergang(1, 1, 0.8);

        for (int i = 0; i < 6; i++) {
            hmm.setEmissionsH‰ufigkeit(0, i, 1.0 / 6.0);
            hmm.setEmissionsH‰ufigkeit(1, i, 0.1);
        }
        hmm.setEmissionsH‰ufigkeit(1, 5, 0.5);

        HMM hmmTrain = new MatrixHMM(new String[] { "F", "U" }, new String[] {
                "1", "2", "3", "4", "5", "6" });

        hmmTrain.setStartH‰ufigkeit(0, 0.5);
        hmmTrain.setStartH‰ufigkeit(1, 0.5);

        hmmTrain.set‹bergang(0, 0, 0.4);
        hmmTrain.set‹bergang(1, 0, 0.6);
        hmmTrain.set‹bergang(0, 1, 0.6);
        hmmTrain.set‹bergang(1, 1, 0.4);

        for (int i = 0; i < 6; i++) {
            hmmTrain.setEmissionsH‰ufigkeit(0, i, 1.0 / 6.0);
            hmmTrain.setEmissionsH‰ufigkeit(1, i, 0.1);
        }
        hmmTrain.setEmissionsH‰ufigkeit(1, 5, 0.5);

        FolgeErzeugen erz = new FolgeErzeugen(hmm);
        pr¸fen(hmm);
        pr¸fen(hmmTrain);

        BaumWelch trainer = new BaumWelch(hmmTrain);

        for (int i = 0; i < 1; i++) {
            HMMSequenceItem[] werte = erz.generate(1000);
            int[] emissionen = HMMSequenceItem.toEmissionen(werte);
            trainer.updateHmm(emissionen);
        }
        ausgabe(hmm);
        ausgabe(hmmTrain);
        pr¸fen(hmmTrain);

    }

    private void pr¸fen(HMM hmm) {
        if (!hmm.testeWerte()) {
            throw new IllegalStateException("keine konsistenten Werte");
        }

    }

    private void ausgabe(HMM model) {
        System.out.println();
        for (int i = 0; i < model.getAnzahlDerZust‰nde(); i++) {
            System.out.println();
            for (int j = 0; j < model.getAnzahlDerZust‰nde(); j++) {
                System.out.print("hmm[" + i + "," + j + "]= "
                        + model.get‹bergang(i, j) + " ");
            }
        }
        System.out.println();
    }

}
