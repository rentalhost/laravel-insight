package net.rentalhost.idea.laravelInsight.eloquent;

import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;

import net.rentalhost.suite.FixtureSuite;

public class ColumnWithoutAnnotationInspectionTest extends FixtureSuite {
    public void testAll() {
        inspectTool(ColumnWithoutAnnotationInspection.class)
            .addTestFile("laravelInsight/eloquent/ColumnWithoutAnnotationInspection.externalReference.php")
            .addTestFile("laravelInsight/eloquent/ColumnWithoutAnnotationInspection.php")
            .highlightTest()
            .quickFixesTest("laravelInsight/eloquent/ColumnWithoutAnnotationInspection.php");

        inspectTool(ColumnWithoutAnnotationInspection.class)
            .addTestFile("laravelInsight/eloquent/ColumnWithoutAnnotationInspection.coverage.php")
            .highlightTest();

        inspectTool(ColumnWithoutAnnotationInspection.class)
            .addTestFile("laravelInsight/eloquent/ColumnWithoutAnnotationInspection.matchTypes.php")
            .highlightTest()
            .quickFixesTest("laravelInsight/eloquent/ColumnWithoutAnnotationInspection.matchTypes.php");
    }

    public void testNPEs() {
        final PsiFile fileSample = getResourceFile("laravelInsight/eloquent/ColumnWithoutAnnotationInspection.npe.php");

        final PhpClass classShouldBeRemovedProgramatically = (PhpClass) getElementByName(fileSample, "ShouldBeRemovedProgramatically");
        runWriteAction(() -> valueOf(classShouldBeRemovedProgramatically.getNameIdentifier()).delete());

        inspectTool(ColumnWithoutAnnotationInspection.class)
            .runVisitor(visitor -> visitor.visitPhpClass(classShouldBeRemovedProgramatically));

        final PhpClassMember fieldTimestamps = (PhpClassMember) getElementByName(fileSample, "timestamps");

        inspectTool(ColumnWithoutAnnotationInspection.class)
            .runVisitor(visitor -> visitor.visitPhpClass(fieldTimestamps.getContainingClass()));

        final PhpClassMember fieldPrimaryKey = (PhpClassMember) getElementByName(fileSample, "primaryKey");

        inspectTool(ColumnWithoutAnnotationInspection.class)
            .runVisitor(visitor -> visitor.visitPhpClass(fieldPrimaryKey.getContainingClass()));
    }
}
