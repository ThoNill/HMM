package thomas.nill.hmm.modell;

public class HMMSequenceItem {
    public HMMSequenceItem(int emiion, int zustand) {
        super();
        this.emission = emiion;
        this.zustand = zustand;
    }

    public int emission;
    public int zustand;

    @Override
    public String toString() {
        return "" + emission + " (" + zustand + ")";
    }

    public static int[] toEmissionen(HMMSequenceItem[] items) {
        int[] erg = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            erg[i] = items[i].emission;
        }
        return erg;
    }

    public static int[] toZustände(HMMSequenceItem[] items) {
        int[] erg = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            erg[i] = items[i].zustand;
        }
        return erg;
    }
}
