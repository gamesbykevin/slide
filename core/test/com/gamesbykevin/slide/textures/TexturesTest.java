package com.gamesbykevin.slide.textures;

import org.junit.Test;

import static org.junit.Assert.*;

public class TexturesTest {

    @Test
    public void testKeys() {

        //keys we use to load a level can't change
        assertEquals(Textures.Key.Key.getFileCharKey(), "A");
        assertEquals(Textures.Key.Locked.getFileCharKey(), "B");
        assertEquals(Textures.Key.RedirectNW.getFileCharKey(), "C");
        assertEquals(Textures.Key.RedirectNE.getFileCharKey(), "D");
        assertEquals(Textures.Key.RedirectSE.getFileCharKey(), "E");
        assertEquals(Textures.Key.RedirectSW.getFileCharKey(), "F");
        assertEquals(Textures.Key.Wall.getFileCharKey(), "H");
        assertEquals(Textures.Key.Danger.getFileCharKey(), "J");
        assertEquals(Textures.Key.Goal.getFileCharKey(), "K");
        assertEquals(Textures.Key.Collected.getFileCharKey(), "L");
        assertEquals(Textures.Key.Player.getFileCharKey(), "M");
    }
}