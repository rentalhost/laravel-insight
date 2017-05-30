package net.rentalhost.idea.api;

import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import org.junit.Assert;

import net.rentalhost.suite.FixtureSuite;

public class PhpDocCommentUtilTest extends FixtureSuite {
    public void testFindProperty() {
        final PsiFile         fileSample      = getResourceFile("api/PhpDocCommentUtil.docProperties.php");
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
}
