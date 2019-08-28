package de.lj.tronsnakebackend.service.game.assets;

public class MyRandom {

    public static int generateInt(int min, int max) {
        return (int) Math.round((min + (max - min) * Math.random()));
    }

    public static int[] generateIntArray(int length, int min, int max) {
        int[] result = new int[length];
        for(int i = 0; i < result.length; i++) {
            result[i] = generateInt(min, max);
        }
        return result;
    }

    public static int[] generateUniqueIntArray(int length, int min, int max) {
        int[] result = new int[length];
        int rndNum;

        if(length < (max - min)) {
            return null;
        }

        for(int i = 0; i < length; i++) {
            result[i] = min - 1;
        }

        for(int i = 0; i < result.length; i++) {
            do {
                rndNum = generateInt(min, max);
            } while(!isNumberUnique(result, rndNum));
            result[i] = rndNum;
        }

        return result;
    }

    private static boolean isNumberUnique(int[] arr, int number) {
        for(int num : arr) {
            if(number == num) return false;
        }
        return true;
    }
}
