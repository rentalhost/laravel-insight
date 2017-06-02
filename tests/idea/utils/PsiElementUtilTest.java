package net.rentalhost.idea.utils;

import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.AssignmentExpression;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import org.junit.Assert;

import net.rentalhost.suite.FixtureSuite;

public class PsiElementUtilTest extends FixtureSuite {
    public void testSkipParentheses() {
        final PsiFile fileSample = getResourceFile("utils/PsiElementUtil.sample.php");

        final PhpNamedElement      referenceVariable   = valueOf(getElementByName(fileSample, "referenceVariable"));
        final AssignmentExpression referenceAssignment = (AssignmentExpression) referenceVariable.getParent();

        Assert.assertEquals("1", valueOf(PsiElementUtil.skipParentheses(valueOf(referenceAssignment.getValue()))).getText());
    }
}
