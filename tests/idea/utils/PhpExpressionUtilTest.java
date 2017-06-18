package net.rentalhost.idea.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.AssignmentExpression;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.junit.Assert;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.suite.FixtureSuite;

public class PhpExpressionUtilTest extends FixtureSuite {
    @NotNull
    private static PhpExpression getStringLiteral(
        @NotNull final PsiElement fileSample,
        final String variableName
    ) {
        return valueOf((PhpExpression) ((AssignmentExpression) getElementByName(fileSample, variableName).getParent()).getValue());
    }

    public void testResolve() {
        final PsiFile fileSample = getResourceFile("utils/PhpExpressionUtil.warpingLiterals.php");

        // Default const types.
        final StringLiteralExpression directLiteral     = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "directLiteral"));
        final StringLiteralExpression indirectLiteral   = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "indirectLiteral"));
        final StringLiteralExpression warpingLiteral    = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "warpingLiteral"));
        final StringLiteralExpression assignedLiteral   = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "assignedLiteral"));
        final StringLiteralExpression withSubAssignment = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "withSubAssignment"));

        Assert.assertEquals("directValue", directLiteral.getContents());

        Assert.assertEquals("indirectValue", indirectLiteral.getContents());
        Assert.assertEquals("indirectValue", warpingLiteral.getContents());
        Assert.assertEquals("indirectValue", assignedLiteral.getContents());
        Assert.assertEquals("indirectValue", withSubAssignment.getContents());

        final PhpExpression ccUnresolvedConstantReference = (PhpExpression) getElementByName(fileSample, "ccUnresolvedConstantReference");

        Assert.assertTrue(PhpExpressionUtil.resolve(ccUnresolvedConstantReference) instanceof ConstantReference);

        // Class const types.
        final StringLiteralExpression classIndirectLiteral = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "classIndirectLiteral"));
        final StringLiteralExpression classWarpingLiteral  = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "classWarpingLiteral"));

        Assert.assertEquals("indirectClassValue", classIndirectLiteral.getContents());
        Assert.assertEquals("indirectClassValue", classWarpingLiteral.getContents());

        final PsiElement classResolvingFromProperty =
            PhpExpressionUtil.resolve(valueOf((PhpExpression) ((Field) getElementByName(fileSample, "resolvingFromProperty")).getDefaultValue()));

        Assert.assertEquals("TRUE", classResolvingFromProperty.getText());

        final PsiElement classResolvingDirectlyFromProperty =
            PhpExpressionUtil.resolve(valueOf((PhpExpression) ((Field) getElementByName(fileSample, "resolvingDirectlyFromProperty")).getDefaultValue()));

        Assert.assertEquals("null", classResolvingDirectlyFromProperty.getText());

        // Avoiding complex loopings.
        final PhpExpression shouldAvoidCyclicLoopingsWithConstants = (PhpExpression) getElementByName(fileSample, "shouldAvoidCyclicLoopingsWithConstants");

        Assert.assertEquals("SHOULD_IGNORES_CYCLIC_LOOPINGS_A", PhpExpressionUtil.resolve(shouldAvoidCyclicLoopingsWithConstants).getText());

        final PhpExpression shouldAvoidCyclicLoopingsWithVariablesA = (PhpExpression) getElementByName(fileSample, "shouldAvoidCyclicLoopingsWithVariablesA");

        Assert.assertEquals("$shouldAvoidCyclicLoopingsWithVariablesA", PhpExpressionUtil.resolve(shouldAvoidCyclicLoopingsWithVariablesA).getText());

        // Resolving variables.
        final StringLiteralExpression variableWrapping = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "variableWrapping"));

        Assert.assertEquals("value", variableWrapping.getContents());

        // With wrapping parentheses.
        final StringLiteralExpression withParanteshesWrapping = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "withParanteshesWrapping"));

        Assert.assertEquals("parentheses", withParanteshesWrapping.getContents());

        // Should not resolve totally.
        final PhpExpression indirectShouldNotResolveTotally = PhpExpressionUtil.resolve(getStringLiteral(fileSample, "indirectShouldNotResolveTotally"));

        Assert.assertTrue(indirectShouldNotResolveTotally instanceof MethodReference);

        // Stop on first ConstantReference.
        final PhpExpression stopOnFirstConstantReference =
            PhpExpressionUtil.resolve(getStringLiteral(fileSample, "stopOnFirstConstantReference"), ConstantReference.class::isInstance);

        Assert.assertTrue(stopOnFirstConstantReference instanceof ConstantReference);
        Assert.assertEquals("$stopOnFirstConstantReference = SHOULD_STOP_HERE", stopOnFirstConstantReference.getParent().getText());

        // Make sure that resolves to a @property will not broke.
        final AssignmentExpression ccInstancePropertyRef = (AssignmentExpression) getElementByName(fileSample, "ccInstancePropertyRef").getParent();
        final PhpExpression        ccInstanceProperty    = PhpExpressionUtil.resolve(valueOf((PhpExpression) ccInstancePropertyRef.getValue()));

        Assert.assertTrue(ccInstanceProperty instanceof MethodReference);
    }
}
