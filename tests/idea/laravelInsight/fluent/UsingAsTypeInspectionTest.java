package net.rentalhost.idea.laravelInsight.fluent;

import net.rentalhost.suite.FixtureSuite;

public class UsingAsTypeInspectionTest extends FixtureSuite {
    public void testAll() {
        inspectTool(UsingAsTypeInspection.class)
            .addTestFile("laravelInsight/fluent/UsingAsTypeInspection.php")
            .highlightTest();
    }
}
