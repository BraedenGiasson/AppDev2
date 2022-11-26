package com.example.invoiceapp_braedengiasson.helpers;

public class RandomNumber {
    /**
     * Gets a random number between the specified values.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return
     */
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
