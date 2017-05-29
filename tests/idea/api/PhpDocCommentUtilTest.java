package net.rentalhost.idea.api;

import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.suite.FixtureSuite;

public class PhpDocCommentUtilTest extends FixtureSuite {
    public void testHasProperty() {
        final List<PhpDocComment> fileDocComments = getPhpDocCommentsResource();

        // Does not exists.
        Assert.assertFalse(PhpDocCommentUtil.hasProperty(fileDocComments.get(0), "paramInexistent"));

        // Does exists.
        Assert.assertTrue(PhpDocCommentUtil.hasProperty(fileDocComments.get(0), "param1"));
        Assert.assertTrue(PhpDocCommentUtil.hasProperty(fileDocComments.get(0), "param2"));
        Assert.assertTrue(PhpDocCommentUtil.hasProperty(fileDocComments.get(0), "param3"));
        Assert.assertTrue(PhpDocCommentUtil.hasProperty(fileDocComments.get(0), "param4"));
    }

    public void testFindProperty() {
        final List<PhpDocComment> fileDocComments = getPhpDocCommentsResource();

        // Does not exists.
        Assert.assertNull(PhpDocCommentUtil.findProperty(fileDocComments.get(0), "paramInexistent"));

        // Does exists.
        Assert.assertNotNull(PhpDocCommentUtil.findProperty(fileDocComments.get(0), "param1"));
        Assert.assertNotNull(PhpDocCommentUtil.findProperty(fileDocComments.get(0), "param2"));
        Assert.assertNotNull(PhpDocCommentUtil.findProperty(fileDocComments.get(0), "param3"));
        Assert.assertNotNull(PhpDocCommentUtil.findProperty(fileDocComments.get(0), "param4"));
    }

    @NotNull
    private List<PhpDocComment> getPhpDocCommentsResource() {
        final PsiFile             fileSample      = getResourceFile("api/PhpDocCommentUtil.docProperties.php");
        final List<PhpDocComment> fileDocComments = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpDocComment.class));

        Assert.assertEquals(1, fileDocComments.size());
        return fileDocComments;
    }
}
