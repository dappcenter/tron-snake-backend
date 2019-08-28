package de.lj.tronsnakebackend.service.game.assets;

public class MyTimer {
    private static long start = 0;
    private static long stop = 0;
    private static boolean running = false;

    public static void start() {
        if(!running) {
            start = System.nanoTime();
            running = true;
        }
    }

    public static void stop() {
        if(running) {
            stop = System.nanoTime();
            running = false;
        }
    }

    public static void reset() {
        start = 0;
        stop = 0;
        running = false;
    }

    public static long getNanoseconds() {
        if(running) return System.nanoTime() - start;
        return start == 0 ? 0 : stop - start;
    }

    public static long getMilliseconds() {
        return getNanoseconds()/1000000;
    }
}
