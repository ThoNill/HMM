package thomas.nill.hmm.modell;

import java.util.Arrays;

public class BaumWelch {

    HMM model;
    int[] emissionen;
    double[][] forwardWerte;
    double[][] backwardWerte;
    double[] skalierung;
    double sequenceH�ufigkeit;

    public BaumWelch(HMM model) {
        super();
        this.model = model;
        testeModel();
    }

    public void updateHmm(int[] emissionen) {
        this.emissionen = emissionen;
        this.forwardWerte = forward(emissionen);
        this.backwardWerte = backward(emissionen);

        System.out.print("\nForward: ");
        ausgabe(forwardWerte);
        System.out.print("\n Backward: ");
        ausgabe(backwardWerte);
        if (!testeGleichheit(forwardWerte[forwardWerte.length - 1],
                backwardWerte[0], skalierung[0])) {
            throw new RuntimeException("Werte sind nicht gleich");
        }
        sequenceH�ufigkeit = model.summe(forwardWerte[forwardWerte.length - 1]);
        System.out.println("sequenceH�ufigkeit= " + sequenceH�ufigkeit);
        System.out.println("summeAnzahlDerTransitionen= "
                + summeAnzahlDerTransitionen());
        for (int t = 0; t < emissionen.length; t++) {
            System.out.println("summeGamma[" + t + "]" + summeGamma(t));
        }
        �ndere�berg�nge();
        �ndereEmissionen();
        testeModel();
    }

    protected void testeModel() {
        if (!model.testeWerte()) {
            throw new IllegalArgumentException("Model ist nicht konsistent");
        }
    }

    private double[][] forward(int[] emissionen) {
        System.out.println("Emissionen: " + Arrays.toString(emissionen));
        System.out.println("\nStart Forward");

        int anzahlEmissionen = emissionen.length;
        int anzahlZust�nde = model.getAnzahlDerZust�nde();

        this.skalierung = new double[anzahlEmissionen+1];
        skalierung[anzahlEmissionen] = 1.0;

        double[][] forwardWerte = new double[anzahlEmissionen][anzahlZust�nde];
        int emission = emissionen[0];
        System.out.println("\nEmission[" + 0 + "] = " + emission);
        for (int zustand = 0; zustand < model.getAnzahlDerZust�nde(); zustand++) {
            forwardWerte[0][zustand] = forwardInit(zustand, emission);
        }
        forwardSkalieren(0, forwardWerte);
        ausgabe(0, forwardWerte[0]);

        for (int t = 0; t < emissionen.length - 1; t++) {
            emission = emissionen[t + 1];
            System.out.println("\nEmission[" + (t + 1) + "] = " + emission);
            for (int zustand = 0; zustand < model.getAnzahlDerZust�nde(); zustand++) {
                forwardWerte[t + 1][zustand] = forwardSchritt(forwardWerte[t],
                        zustand, emission, t);
            }
            forwardSkalieren(t + 1, forwardWerte);

            ausgabe(t + 1, forwardWerte[t + 1]);
            // normieren(forwardWerte[i]);
        }
        return forwardWerte;
    }

    protected void forwardSkalieren(int t, double[][] forwardWerte) {
        skalierung[t] = skalierung(forwardWerte[t]);
        skalieren(skalierung[t], forwardWerte[t]);
    }

    private double skalierung(double[] werte) {
        double summe = 0.0;
        for (int z = 0; z < model.getAnzahlDerZust�nde(); z++) {
            summe += werte[z];
        }
        return 1 / summe;
    }

    private double skalieren(double skalierung, double[] werte) {
        double summe = 0.0;
        for (int z = 0; z < model.getAnzahlDerZust�nde(); z++) {
            werte[z] = skalierung * werte[z];
        }
        return summe;
    }

    private double forwardInit(int zustand, int emission) {
        double startH�ufigkeit = model.getStartH�ufigkeit(zustand);
        double emissionsH�ufigkeit = model.getEmissionsH�ufigkeit(zustand,
                emission);
        return startH�ufigkeit * emissionsH�ufigkeit;
    }

    private double forwardSchritt(double[] forwardWerte, int zustand,
            int emission, int index) {
        double summe = 0.0;
        for (int nz = 0; nz < model.getAnzahlDerZust�nde(); nz++) {
            double h = model.get�bergang(nz, zustand);
            double wert = forwardWerte[nz] * h;
            summe += wert;
        }
        double emissionsH�ufigkeit = model.getEmissionsH�ufigkeit(zustand,
                emission);
        return summe * emissionsH�ufigkeit;
    }

    private double[][] backward(int[] emissionen) {
        System.out.println("\nStart Backward");
        int anzahlEmissionen = emissionen.length;
        int anzahlZust�nde = model.getAnzahlDerZust�nde();

        double[][] backwardWerte = new double[anzahlEmissionen + 1][anzahlZust�nde];

        int pos = backwardWerte.length - 1;
        for (int zustand = 0; zustand < model.getAnzahlDerZust�nde(); zustand++) {
            backwardWerte[pos][zustand] = model.getStartH�ufigkeit(zustand);
        }
        ausgabe(pos, backwardWerte[pos]);
        for (int t = pos; t > 0; t--) {
            int emission = emissionen[t - 1];
            System.out.println("\nEmission[" + t + "] = " + emission);
            for (int zustand = 0; zustand < model.getAnzahlDerZust�nde(); zustand++) {
                backwardWerte[t - 1][zustand] = backwardSchritt(
                        backwardWerte[t], zustand, emission, t);
            }
            backwardSkalieren(anzahlEmissionen, t - 1, backwardWerte);
            ausgabe(t - 1, backwardWerte[t - 1]);
        }

        return backwardWerte;
    }

    private double backwardSchritt(double[] backwardWerte, int zustand,
            int emission, int index) {
        double summe = 0.0;
        for (int nz = 0; nz < model.getAnzahlDerZust�nde(); nz++) {
            double h = model.get�bergang(zustand, nz);
            double emissionsH�ufigkeit = model.getEmissionsH�ufigkeit(nz,
                    emission);
            double wert = backwardWerte[nz] * h * emissionsH�ufigkeit ;
            summe += wert;
        }
        return summe;
    }

    protected void backwardSkalieren(int anzahlEmissionen, int t,
            double[][] backwardWerte) {
        skalieren(skalierung[t+1], backwardWerte[t]);
    }

    private void �ndere�berg�nge() {
        int anz = model.getAnzahlDerZust�nde();
        for (int i = 0; i < anz; i++) {
            for (int j = 0; j < anz; j++) {
                double alt = model.get�bergang(i, j);
                double neu = berechne�bergang(i, j);

                System.out.println("�bergang[" + i + "," + j + "]= alt= " + alt
                        + " neu= " + neu);

                model.set�bergang(i, j, (neu + alt) / 2.0);
            }
        }
    }

    private void �ndereEmissionen() {
        for (int z = 0; z < model.getAnzahlDerZust�nde(); z++) {
            for (int e = 0; e < model.getAnzahlDerEmissionen(); e++) {
                double alt = model.getEmissionsH�ufigkeit(z, e);
                double neu = berechneEmissionsH�ufigkeit(z, e);

                model.setEmissionsH�ufigkeit(z, e, (alt + neu) / 2.0);
            }
        }
    }

    private double gamma(int zustand, int t) {
        double wert = forwardWerte[t][zustand] * backwardWerte[t][zustand]
                / sequenceH�ufigkeit;
        // System.out.println("gamma[" + zustand + "," + t + "]}"+wert);

        return wert;
    }

    private double xi(int zustandA, int zustandB, int t) {
        int emission = emissionen[t];
        return (forwardWerte[t][zustandA]
                * model.get�bergang(zustandA, zustandB)
                * model.getEmissionsH�ufigkeit(zustandB, emission) * backwardWerte[t + 1][zustandB]
                * skalierung[t+1])
                / sequenceH�ufigkeit;
    }

    private double anzahlDerTransitionenImZustand(int zustand, int bisT) {
        double summe = 0.0;
        for (int t = 0; t < bisT; t++) {
            summe += gamma(zustand, t);
        }
        return summe;
    }

    private double berechne�bergang(int zustandA, int zustandB) {
        double vonAnachB = anzahlDerTransitionenVonAnachB(zustandA, zustandB);
        double vonA = anzahlDerTransitionenVonZustandA(zustandA);
        double wert = div(vonAnachB, vonA);
        System.out.println("�bergang[" + zustandA + "," + zustandB + "] = "
                + wert + " vonAnachB= " + vonAnachB + "/ vonA= " + vonA);
        return wert;

    }

    private double anzahlDerTransitionenVonAnachB(int zustandA, int zustandB) {
        double summe = 0.0;
        for (int t = 0; t < emissionen.length - 1; t++) {
            summe += xi(zustandA, zustandB, t);
        }
        return summe;
    }

    private double anzahlDerTransitionenVonZustandA(int zustand) {
        return anzahlDerTransitionenImZustand(zustand, emissionen.length - 1);
    }

    private double berechneEmissionsH�ufigkeit(int zustand, int emission) {
        double wert = div(
                anzahlDerTransitionenVonZustandAmitEmmission(zustand, emission),
                anzahlDerTransitionenImZustandA(zustand));
        System.out.println("Emission[" + zustand + "," + emission + "]= "
                + wert);
        return wert;
    }

    private double anzahlDerTransitionenVonZustandAmitEmmission(int zustand,
            int emission) {
        double summe = 0.0;
        for (int t = 0; t < emissionen.length; t++) {
            if (emissionen[t] == emission) {
                summe += gamma(zustand, t);
            }
        }
        return summe;
    }

    private double anzahlDerTransitionenImZustandA(int zustand) {
        return anzahlDerTransitionenImZustand(zustand, emissionen.length);
    }

    private double div(double a, double b) {
        if (Math.abs(b) >= 0.0000000000001) {
            return a / b;
        }
        return 0.0;
    }

    private void ausgabe(double[][] werte) {
        System.out.println();
        for (int i = 0; i < werte.length; i++) {
            ausgabe(i, werte[i]);
        }
    }

    private void ausgabe(int pos, double[] werte) {
        System.out.println();
        System.out.print("[" + pos + "] ");
        for (int i = 0; i < werte.length; i++) {
            System.out.print(werte[i] + " ");
        }
        double sum = model.summe(werte);
        System.out.print(" S= " + sum);
    }

    private boolean testeGleichheit(double[] erwartet, double[] werte,
            double faktor) {
        double sum1 = model.summe(erwartet);
        double sum2 = model.summe(werte);
        System.out.println(" sum1 =" + sum1 + " sum2 = " + sum2);
        return model.vergleich(sum1, sum2 * faktor);
    }

    private double summeAnzahlDerTransitionen() {
        double summe = 0.0;
        for (int z = 0; z < model.getAnzahlDerZust�nde(); z++) {
            summe += anzahlDerTransitionenImZustandA(z);
        }
        return summe;
    }

    private double summeGamma(int t) {
        double summe = 0.0;
        for (int z = 0; z < model.getAnzahlDerZust�nde(); z++) {
            summe += gamma(z, t);
        }
        return summe;
    }
}
