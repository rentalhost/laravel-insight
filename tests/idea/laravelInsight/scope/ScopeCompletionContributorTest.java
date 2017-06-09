package net.rentalhost.idea.laravelInsight.scope;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import org.junit.Assert;

import net.rentalhost.suite.FixtureSuite;

public class ScopeCompletionContributorTest extends FixtureSuite {
    public void testCodeCompletion() {
        final PsiFile fileSample = getResourceFile("laravelInsight/scope/ScopeCompletionContributor.samples.php");

        // Reference 1 (acceptable: field reference to scoped method).
        final PhpNamedElement reference1          = getElementByName(fileSample, "reference1");
        final LookupElement[] completionElements1 = getCompletionElements(reference1, 29);

        Assert.assertEquals("test1", completionElements1[3].getLookupString());
        Assert.assertEquals("test2", completionElements1[4].getLookupString());
        Assert.assertEquals("test3", completionElements1[5].getLookupString());

        coverageHandleInsert(fileSample, completionElements1[3]);

        moveCaret(reference1.getTextOffset() + 29);
        acceptLookupElement(completionElements1[3]);

        Assert.assertEquals("$reference1 = (new Example)->test1()", reference1.getParent().getText());

        // Reference 2 (acceptable: method reference to scoped method).
        final PhpNamedElement reference2          = getElementByName(fileSample, "reference2");
        final LookupElement[] completionElements2 = getCompletionElements(reference2, 29);

        Assert.assertEquals("test1", completionElements2[3].getLookupString());
        Assert.assertEquals("test2", completionElements2[4].getLookupString());
        Assert.assertEquals("test3", completionElements2[5].getLookupString());

        coverageHandleInsert(fileSample, completionElements2[4]);

        moveCaret(reference2.getTextOffset() + 29);
        acceptLookupElement(completionElements2[4]);

        Assert.assertEquals("$reference2 = (new Example)->test2()", reference2.getParent().getText());

        // Reference 3 (not acceptable: reference is not instance of Eloquent\Model).
        final PhpNamedElement reference3          = getElementByName(fileSample, "reference3");
        final LookupElement[] completionElements3 = getCompletionElements(reference3, 33);

        Assert.assertEquals(1, completionElements3.length);

        // Reference 4 (not acceptable: can't resolve class reference).
        final PhpNamedElement reference4          = getElementByName(fileSample, "reference4");
        final LookupElement[] completionElements4 = getCompletionElements(reference4, 37);

        Assert.assertEquals(0, completionElements4.length);

        // Reference 5 (not acceptable: not is an instance of some class - invalid reference).
        final PhpNamedElement reference5          = getElementByName(fileSample, "reference5");
        final LookupElement[] completionElements5 = getCompletionElements(reference5, 35);

        Assert.assertEquals(0, completionElements5.length);
    }
}
