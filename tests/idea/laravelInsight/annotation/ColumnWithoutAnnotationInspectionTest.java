package net.rentalhost.idea.laravelInsight.annotation;

import net.rentalhost.suite.FixtureSuite;

public class ColumnWithoutAnnotationInspectionTest extends FixtureSuite {
    public void testAll() {
        inspectTool(ColumnWithoutAnnotationInspection.class)
            .addTestFile("laravelInsight/annotation/ColumnWithoutAnnotationInspection.externalReference.php")
            .addTestFile("laravelInsight/annotation/ColumnWithoutAnnotationInspection.php")
            .highlightTest()
            .quickFixesTest("laravelInsight/annotation/ColumnWithoutAnnotationInspection.php");

        inspectTool(ColumnWithoutAnnotationInspection.class)
            .addTestFile("laravelInsight/annotation/ColumnWithoutAnnotationInspection.coverage.php")
            .highlightTest();

        inspectTool(ColumnWithoutAnnotationInspection.class)
            .addTestFile("laravelInsight/annotation/ColumnWithoutAnnotationInspection.matchTypes.php")
            .highlightTest()
            .quickFixesTest("laravelInsight/annotation/ColumnWithoutAnnotationInspection.matchTypes.php");
    }
}
