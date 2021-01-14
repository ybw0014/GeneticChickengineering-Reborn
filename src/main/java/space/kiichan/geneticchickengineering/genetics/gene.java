package space.kiichan.geneticchickengineering.genetics;

import java.util.Arrays;

public class gene {
    public char[] alleles;

    public gene(char[] markup) {
        this.alleles = markup;
        Arrays.sort(alleles);
    }

    public gene(char markup, int state) {
        this.alleles = new char[]{markup, markup};
        for (int i=0; i<2; i++) {
            if ((state & i+1) == (i+1)){
                this.alleles[i] = Character.toUpperCase(this.alleles[i]);
            }
        }
    }

    public int getState() {
        /* Returns an int to represent the state of this gene
         * 0 = double recessive
         * 1 = single dominant
         * 3 = double dominant
         */
        int state = 0;
        for (int i=0; i<2; i++) {
            if (Character.isUpperCase(alleles[i])) {
                state = state + i+1;
            }
        }
        return state;
    }

    public boolean isDominant() {
        return getState() > 0;
    }

    public char split() {
        int ix = 0;
        if (Math.random() > 0.5) {
            ix = 1;
        }
        return alleles[ix];
    }

    public String toString() {
        return new String(alleles);
    }

}

