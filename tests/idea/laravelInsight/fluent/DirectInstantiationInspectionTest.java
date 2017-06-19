package net.rentalhost.idea.laravelInsight.fluent;

import net.rentalhost.suite.FixtureSuite;

public class DirectInstantiationInspectionTest extends FixtureSuite {
    public void testAll() {
        inspectTool(DirectInstantiationInspection.class)
            .addTestFile("laravelInsight/fluent/DirectInstantiationInspection.php")
            .highlightTest();
    }
}
