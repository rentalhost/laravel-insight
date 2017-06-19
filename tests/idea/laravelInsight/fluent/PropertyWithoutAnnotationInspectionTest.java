package net.rentalhost.idea.laravelInsight.fluent;

import net.rentalhost.suite.FixtureSuite;

public class PropertyWithoutAnnotationInspectionTest extends FixtureSuite {
    public void testAll() {
        inspectTool(PropertyWithoutAnnotationInspection.class)
            .addTestFile("laravelInsight/fluent/PropertyWithoutAnnotationInspection.php")
            .highlightTest();
    }
}
