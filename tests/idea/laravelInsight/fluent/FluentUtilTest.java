package net.rentalhost.idea.laravelInsight.fluent;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.junit.Assert;

import net.rentalhost.suite.FixtureSuite;

public class FluentUtilTest extends FixtureSuite {
    public void testIsUsingDirectly() {
        Assert.assertFalse(FluentUtil.isUsingDirectly(null));

        final PsiFile    fileSample     = getResourceFile("laravelInsight/fluent/FluentUtil.samples.php");
        final PsiElement emptyReference = getElementByName(fileSample, "reference");

        Assert.assertFalse(FluentUtil.isUsingDirectly(emptyReference));
    }

    public void testIsUsingIndirectly() {
        Assert.assertFalse(FluentUtil.isUsingIndirectly(null));

        final PsiFile    fileSample     = getResourceFile("laravelInsight/fluent/FluentUtil.samples.php");
        final PsiElement emptyReference = getElementByName(fileSample, "reference");

        Assert.assertFalse(FluentUtil.isUsingIndirectly(emptyReference));
    }
}
