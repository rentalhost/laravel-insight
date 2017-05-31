package net.rentalhost.idea.laravelInsight.annotation;

import net.rentalhost.suite.FixtureSuite;

public class PropertyWithoutAnnotationInspectionTest extends FixtureSuite {
    public void testAll() {
        inspectTool(PropertyWithoutAnnotationInspection.class)
            .addTestFile("laravelInsight/annotation/PropertyWithoutAnnotation.externalReference.php")
            .addTestFile("laravelInsight/annotation/PropertyWithoutAnnotation.php")
            .highlightTest();
    }
}
