package com.gamesbykevin.slide.guid;

/**
 * Generate random id to prevent GWT compilation errors
 */
public class GUID {

    /**
     * All valid characters in our GUID
     */
    private static final char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    //our guid will be 36 characters long
    private static final int SIZE = 36;

    /**
     * Generate a random GUID
     * @return
     */
    public static String generate() {

        char[] uuid = new char[36];

        for (int i = 0; i < uuid.length; i++) {

            //pick random index
            int index = (int)(Math.random() * CHARS.length);

            //populate our array
            uuid[i] = CHARS[index];
        }

        return new String(uuid);
    }
}