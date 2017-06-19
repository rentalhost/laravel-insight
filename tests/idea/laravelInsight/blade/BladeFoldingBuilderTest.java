package net.rentalhost.idea.laravelInsight.blade;

import net.rentalhost.suite.FixtureSuite;

public class BladeFoldingBuilderTest extends FixtureSuite {
    public void testBuildFoldRegions() {
        testFolding("laravelInsight/blade/BladeFoldingBuilder.samples.blade.php");
    }
}
