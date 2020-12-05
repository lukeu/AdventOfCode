package aoc15;

import util.FUtils;

public class Old1 {
    public static void main(String[] args) {
        new Old1().go();
    }

    void go() {
        String str = FUtils.readLine(2015, 1);
        System.out.println(str.chars().mapToLong(c -> (c == '(' ? 1 : (c == ')' ? -1 : 0))).sum());

        int off = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int delta = (c == '(' ? 1 : (c == ')' ? -1 : 0));
            off += delta;
            if (off < 0) {
                System.out.println(i+1);
                return;
            }
        }

    }
}
