package net.rentalhost.idea.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocProperty;
import com.jetbrains.php.lang.psi.elements.AssignmentExpression;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.junit.Assert;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.suite.FixtureSuite;

public class PsiElementUtilTest extends FixtureSuite {
    @NotNull
    private static PsiElement getStringLiteral(
        @NotNull final PsiElement fileSample,
        final String variableName
    ) {
        return valueOf((PhpExpression) ((AssignmentExpression) getElementByName(fileSample, variableName).getParent()).getValue());
    }

    public void testSkipParentheses() {
        final PsiFile fileSample = getResourceFile("utils/PsiElementUtil.sample.php");

        final PhpNamedElement      referenceVariable   = valueOf(getElementByName(fileSample, "referenceVariable"));
        final AssignmentExpression referenceAssignment = (AssignmentExpression) referenceVariable.getParent();

        Assert.assertEquals("1", valueOf(PsiElementUtil.skipParentheses(valueOf(referenceAssignment.getValue()))).getText());
    }

    public void testResolve() {
        final PsiFile fileSample = getResourceFile("utils/PsiElementUtil.warpingLiterals.php");

        // Default const types.
        final StringLiteralExpression directLiteral     = (StringLiteralExpression) PsiElementUtil.resolve(getStringLiteral(fileSample, "directLiteral"));
        final StringLiteralExpression indirectLiteral   = (StringLiteralExpression) PsiElementUtil.resolve(getStringLiteral(fileSample, "indirectLiteral"));
        final StringLiteralExpression warpingLiteral    = (StringLiteralExpression) PsiElementUtil.resolve(getStringLiteral(fileSample, "warpingLiteral"));
        final StringLiteralExpression assignedLiteral   = (StringLiteralExpression) PsiElementUtil.resolve(getStringLiteral(fileSample, "assignedLiteral"));
        final StringLiteralExpression withSubAssignment = (StringLiteralExpression) PsiElementUtil.resolve(getStringLiteral(fileSample, "withSubAssignment"));

        Assert.assertEquals("directValue", directLiteral.getContents());

        Assert.assertEquals("indirectValue", indirectLiteral.getContents());
        Assert.assertEquals("indirectValue", warpingLiteral.getContents());
        Assert.assertEquals("indirectValue", assignedLiteral.getContents());
        Assert.assertEquals("indirectValue", withSubAssignment.getContents());

        final PsiElement ccUnresolvedConstantReference = getElementByName(fileSample, "ccUnresolvedConstantReference");

        Assert.assertTrue(PsiElementUtil.resolve(ccUnresolvedConstantReference) instanceof ConstantReference);

        // Class const types.
        final StringLiteralExpression classIndirectLiteral = (StringLiteralExpression) PsiElementUtil.resolve(getStringLiteral(fileSample, "classIndirectLiteral"));
        final StringLiteralExpression classWarpingLiteral  = (StringLiteralExpression) PsiElementUtil.resolve(getStringLiteral(fileSample, "classWarpingLiteral"));

        Assert.assertEquals("indirectClassValue", classIndirectLiteral.getContents());
        Assert.assertEquals("indirectClassValue", classWarpingLiteral.getContents());

        final PsiElement classResolvingFromProperty =
            PsiElementUtil.resolve(valueOf((PhpExpression) ((Field) getElementByName(fileSample, "resolvingFromProperty")).getDefaultValue()));

        Assert.assertEquals("TRUE", classResolvingFromProperty.getText());

        final PsiElement classResolvingDirectlyFromProperty =
            PsiElementUtil.resolve(valueOf((PhpExpression) ((Field) getElementByName(fileSample, "resolvingDirectlyFromProperty")).getDefaultValue()));

        Assert.assertEquals("null", classResolvingDirectlyFromProperty.getText());

        // Avoiding complex loopings.
        final PsiElement shouldAvoidCyclicLoopingsWithConstants = getElementByName(fileSample, "shouldAvoidCyclicLoopingsWithConstants");

        Assert.assertEquals("SHOULD_IGNORES_CYCLIC_LOOPINGS_A", PsiElementUtil.resolve(shouldAvoidCyclicLoopingsWithConstants).getText());

        final PsiElement shouldAvoidCyclicLoopingsWithVariablesA = getElementByName(fileSample, "shouldAvoidCyclicLoopingsWithVariablesA");

        Assert.assertEquals("$shouldAvoidCyclicLoopingsWithVariablesA", PsiElementUtil.resolve(shouldAvoidCyclicLoopingsWithVariablesA).getText());

        // Resolving variables.
        final StringLiteralExpression variableWrapping = (StringLiteralExpression) PsiElementUtil.resolve(getStringLiteral(fileSample, "variableWrapping"));

        Assert.assertEquals("value", variableWrapping.getContents());

        // With wrapping parentheses.
        final StringLiteralExpression withParanteshesWrapping = (StringLiteralExpression) PsiElementUtil.resolve(getStringLiteral(fileSample, "withParanteshesWrapping"));

        Assert.assertEquals("parentheses", withParanteshesWrapping.getContents());

        // Should not resolve totally.
        final PsiElement indirectShouldNotResolveTotally = PsiElementUtil.resolve(getStringLiteral(fileSample, "indirectShouldNotResolveTotally"));

        Assert.assertTrue(indirectShouldNotResolveTotally instanceof MethodReference);

        // Stop on first ConstantReference.
        final PsiElement stopOnFirstConstantReference =
            PsiElementUtil.resolve(getStringLiteral(fileSample, "stopOnFirstConstantReference"), ConstantReference.class::isInstance);

        Assert.assertTrue(stopOnFirstConstantReference instanceof ConstantReference);
        Assert.assertEquals("$stopOnFirstConstantReference = SHOULD_STOP_HERE", stopOnFirstConstantReference.getParent().getText());

        final PsiElement stopOnFirstConstantReferenceIndirect =
            PsiElementUtil.resolve(getStringLiteral(fileSample, "stopOnFirstConstantReferenceIndirect"), ConstantReference.class::isInstance);

        Assert.assertTrue(stopOnFirstConstantReferenceIndirect instanceof ConstantReference);
        Assert.assertEquals("$stopOnFirstConstantReference = SHOULD_STOP_HERE", stopOnFirstConstantReferenceIndirect.getParent().getText());

        // Make sure that resolves to a @property will not broke.
        final AssignmentExpression ccInstancePropertyRef = (AssignmentExpression) getElementByName(fileSample, "ccInstancePropertyRef").getParent();
        final PsiElement           ccInstanceProperty    = PsiElementUtil.resolve(valueOf((PhpExpression) ccInstancePropertyRef.getValue()));

        Assert.assertTrue(ccInstanceProperty instanceof PhpDocProperty);
    }
}
