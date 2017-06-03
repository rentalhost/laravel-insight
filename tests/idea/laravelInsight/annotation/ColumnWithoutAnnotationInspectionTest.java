package net.rentalhost.idea.laravelInsight.annotation;

import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;

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

    public void testNPEs() {
        final PsiFile fileSample = getResourceFile("laravelInsight/annotation/ColumnWithoutAnnotationInspection.npe.php");

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
