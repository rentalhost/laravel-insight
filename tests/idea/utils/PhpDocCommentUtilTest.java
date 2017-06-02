package net.rentalhost.idea.utils;

import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import org.junit.Assert;

import net.rentalhost.suite.FixtureSuite;

public class PhpDocCommentUtilTest extends FixtureSuite {
    public void testFindProperty() {
        final PsiFile         fileSample      = getResourceFile("utils/PhpDocCommentUtil.docProperties.php");
        final PhpNamedElement fileReference   = getElementByName(fileSample, "reference");
        final PhpDocComment   fileDocComments = valueOf(fileReference.getDocComment());

        // Does not exists.
        Assert.assertNull(PhpDocCommentUtil.findProperty(fileDocComments, "paramInexistent"));

        // Does exists.
        Assert.assertNotNull(PhpDocCommentUtil.findProperty(fileDocComments, "param1"));
        Assert.assertNotNull(PhpDocCommentUtil.findProperty(fileDocComments, "param2"));
        Assert.assertNotNull(PhpDocCommentUtil.findProperty(fileDocComments, "param3"));
        Assert.assertNotNull(PhpDocCommentUtil.findProperty(fileDocComments, "param4"));
    }

    public void testCreateTag() {
        final PsiFile fileSample = getResourceFile("utils/PhpDocCommentUtil.tagsCreation.php");

        final PhpNamedElement classClassReference_WithoutAnyPhpDoc = getElementByName(fileSample, "ClassReference_WithoutAnyPhpDoc");

        final PhpDocTag classIdProperty =
            runWriteAction(() -> PhpDocCommentUtil.createTag(classClassReference_WithoutAnyPhpDoc, "@property", "$id"));

        Assert.assertEquals("@property $id\n",
                            valueOf(classIdProperty).getText());
        Assert.assertEquals("/**\n * @property $id\n */",
                            valueOf(classClassReference_WithoutAnyPhpDoc.getDocComment()).getText());

        final PhpDocTag classAnotherProperty =
            runWriteAction(() -> PhpDocCommentUtil.createTag(classClassReference_WithoutAnyPhpDoc, "@property", "$anotherProperty"));

        Assert.assertEquals("@property $anotherProperty\n",
                            valueOf(classAnotherProperty).getText());
        Assert.assertEquals("/**\n * @property $id\n * @property $anotherProperty\n */",
                            valueOf(classClassReference_WithoutAnyPhpDoc.getDocComment()).getText());

        final PhpNamedElement classClassReference_WithPhpDoc = getElementByName(fileSample, "ClassReference_WithPhpDoc");

        final PhpDocTag classPropertyA =
            runWriteAction(() -> PhpDocCommentUtil.createTag(classClassReference_WithPhpDoc, "@propertyA"));

        Assert.assertEquals("@propertyA\n",
                            valueOf(classPropertyA).getText());
        Assert.assertEquals("/**\n * @propertyA\n * @propertyA\n * @propertyB\n */",
                            valueOf(classClassReference_WithPhpDoc.getDocComment()).getText());

        final PhpDocTag classPropertyB =
            runWriteAction(() -> PhpDocCommentUtil.createTag(classClassReference_WithPhpDoc, "@propertyB"));

        Assert.assertEquals("@propertyB\n",
                            valueOf(classPropertyB).getText());
        Assert.assertEquals("/**\n * @propertyA\n * @propertyA\n * @propertyB\n * @propertyB\n */",
                            valueOf(classClassReference_WithPhpDoc.getDocComment()).getText());

        final PhpDocTag classPropertyC =
            runWriteAction(() -> PhpDocCommentUtil.createTag(classClassReference_WithPhpDoc, "@propertyC"));

        Assert.assertEquals("@propertyC\n",
                            valueOf(classPropertyC).getText());
        Assert.assertEquals("/**\n * @propertyA\n * @propertyA\n * @propertyB\n * @propertyB\n * @propertyC\n */",
                            valueOf(classClassReference_WithPhpDoc.getDocComment()).getText());

        final PhpNamedElement classClassReference_WithEmptyPhpDoc = getElementByName(fileSample, "ClassReference_WithEmptyPhpDoc");

        final PhpDocTag classSomeProperty =
            runWriteAction(() -> PhpDocCommentUtil.createTag(classClassReference_WithEmptyPhpDoc, "@someProperty"));

        Assert.assertEquals("@someProperty\n",
                            valueOf(classSomeProperty).getText());
        Assert.assertEquals("/**\n * @someProperty\n */",
                            valueOf(classClassReference_WithEmptyPhpDoc.getDocComment()).getText());

        final PhpNamedElement classClassReference_WithInlinePhpDoc = getElementByName(fileSample, "ClassReference_WithInlinePhpDoc");

        final PhpDocTag classSomeProperty2 =
            runWriteAction(() -> PhpDocCommentUtil.createTag(classClassReference_WithInlinePhpDoc, "@someProperty"));

        Assert.assertEquals("@someProperty\n",
                            valueOf(classSomeProperty2).getText());
        Assert.assertEquals("/** @property\n * @someProperty\n */",
                            valueOf(classClassReference_WithInlinePhpDoc.getDocComment()).getText());

        final PhpNamedElement classClassReference_WithRealworldPhpdoc = getElementByName(fileSample, "ClassReference_WithRealworldPhpdoc");

        final PhpDocTag classSomeProperty3 =
            runWriteAction(() -> PhpDocCommentUtil.createTag(classClassReference_WithRealworldPhpdoc, "@someProperty"));

        Assert.assertEquals("@someProperty\n",
                            valueOf(classSomeProperty3).getText());
        Assert.assertEquals("/**\n * Realworld example.\n * @param int $id\n * @return stdClass\n * @someProperty\n */",
                            valueOf(classClassReference_WithRealworldPhpdoc.getDocComment()).getText());
    }
}
