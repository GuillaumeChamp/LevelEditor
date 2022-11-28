package com.application;

import com.application.Game.Level.LevelElements.Layer1.Collision;
import com.application.Game.Level.LevelElements.Layer1.Encounter;
import com.application.Game.Level.LevelElements.Layer1.OverTile;
import com.application.Game.Level.LevelElements.Layer1.Warp;
import com.application.IO.JSONIO;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Arrays;

@DisplayName("Checking save and load")
public class SerializeTestCase {
    static int testPassed;
    static int testToPass=1;
    @BeforeAll
    static void beforeAll(){
        System.out.println("Testing serialization/deserialization features...");
        testPassed=0;
    }
    @BeforeEach
    void beforeEach(){
        System.out.println(testPassed+"/"+testToPass+" test passed");
    }

    @Test
    @DisplayName("Testing JSON serialization/deserialization")
    public void test() {
        OverTile[][] overTiles = new OverTile[20][10];
        overTiles[1][2] = new Warp("hub",2,3);
        overTiles[0][1] = new Collision();
        overTiles[0][4] = new Encounter(40);
        try {
            JSONIO.saveOverTiles(overTiles, "src/test/resources/testSerialization.json","testSerialization");
            OverTile[][] ans = JSONIO.LoadOverTiles("src/test/resources/testSerialization.json");
            Assertions.assertTrue(Arrays.deepEquals(ans,overTiles));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
