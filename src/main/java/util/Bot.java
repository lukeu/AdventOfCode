package util;

/**
 * Helper class that can walk in 90 deg angles around a 2D grid (origin top-left)
 */
public class Bot {

    enum Dir {
        E(1,0),
        S(0,1),
        W(-1,0),
        N(0,-1);

        private Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
        int dx;
        int dy;
    }

    int x = 0;
    int y = 0;
    Dir dir = Dir.E;

    public void left() {
        dir = switch(dir) {
            case E -> Dir.N;
            case N -> Dir.W;
            case W -> Dir.S;
            case S -> Dir.E;
        };
    }
    public void right() {
        dir = switch(dir) {
            case E -> Dir.S;
            case N -> Dir.E;
            case W -> Dir.N;
            case S -> Dir.W;
        };
    }
    public void forward() {
        move(1);
    }
    public void back() {
        move(-1);
    }
    public void move(int steps) {
        x += (dir.dx * steps);
        y += (dir.dy * steps);
    }
}
