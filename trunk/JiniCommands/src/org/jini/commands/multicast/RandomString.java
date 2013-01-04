
package org.jini.commands.multicast;


class RandomString {

    public static String randomstring(int lo, int hi) {
        int n = rand(lo, hi);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = (byte) rand('A', 'Z');
        }
        return new String(b, 0);
    }

    private static int rand(int lo, int hi) {
        java.util.Random rn = new java.util.Random();
        int n = hi - lo + 1;
        int i = rn.nextInt(n);
        if (i < 0) {
            i = -i;
        }
        return lo + i;
    }

    public static String randomstring() {
        return randomstring(0, 1500);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(randomstring());

    }
}