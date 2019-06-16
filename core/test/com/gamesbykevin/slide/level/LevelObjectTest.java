package com.gamesbykevin.slide.level;

import com.gamesbykevin.slide.level.objects.LevelObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class LevelObjectTest {

    @Test
    public void testId() {

        //create a bunch of objects
        for (int i = 0; i < 10; i++) {

            com.gamesbykevin.slide.level.objects.LevelObject obj1 = new com.gamesbykevin.slide.level.objects.LevelObject(null);
            assertNotEquals(obj1.getId(), 0);
            assertEquals(obj1.getRelatedId(), 0);

            for (int j = 0; j < 10; j++) {

                com.gamesbykevin.slide.level.objects.LevelObject obj2 = new LevelObject(null);
                assertNotEquals(obj1.getId(), obj2.getId());
                assertNotEquals(obj2.getId(), 0);
                assertEquals(obj2.getRelatedId(), 0);
            }
        }
    }
}