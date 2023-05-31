package RealVersion;

import java.util.Arrays;

public class Airport implements Comparable<Airport>{
    String name;
    String[] data;


    @Override
    public int compareTo(Airport o) {
        char[] first = this.name.toCharArray();
        char[] second = o.name.toCharArray();

        int cycleSize = Math.min(first.length, second.length);

        for (int i = 0; i < cycleSize; i++) {
            String s1 = String.valueOf(first[i]);
            String s2 = String.valueOf(second[i]);
            if (!s1.equalsIgnoreCase(s2)) {
                if (first[i] < second[i]) {
                    return -1;
                } else if (first[i] > second[i]) {
                    return 1;
                }
            }
        }

        return Integer.compare(first.length, second.length);
    }

    @Override
    public String toString() {
        return name + Arrays.toString(data);
    }
}
