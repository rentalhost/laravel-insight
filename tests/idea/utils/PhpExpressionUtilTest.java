package net.rentalhost.idea.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.AssignmentExpression;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.Field;
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

    public void testFrom() {
        final PsiFile fileSample = getResourceFile("utils/PhpExpressionUtil.warpingLiterals.php");

        // Default const types.
        final StringLiteralExpression directLiteral     = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "directLiteral"));
        final StringLiteralExpression indirectLiteral   = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "indirectLiteral"));
        final StringLiteralExpression warpingLiteral    = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "warpingLiteral"));
        final StringLiteralExpression assignedLiteral   = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "assignedLiteral"));
        final StringLiteralExpression withSubAssignment = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "withSubAssignment"));

        Assert.assertEquals("directValue", valueOf(directLiteral).getContents());

        Assert.assertEquals("indirectValue", valueOf(indirectLiteral).getContents());
        Assert.assertEquals("indirectValue", valueOf(warpingLiteral).getContents());
        Assert.assertEquals("indirectValue", valueOf(assignedLiteral).getContents());
        Assert.assertEquals("indirectValue", valueOf(withSubAssignment).getContents());

        final PhpExpression ccUnresolvedConstantReference = (PhpExpression) getElementByName(fileSample, "ccUnresolvedConstantReference");

        Assert.assertNull(PhpExpressionUtil.resolve(ccUnresolvedConstantReference));

        // Class const types.
        final StringLiteralExpression classIndirectLiteral = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "classIndirectLiteral"));
        final StringLiteralExpression classWarpingLiteral  = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "classWarpingLiteral"));

        Assert.assertEquals("indirectClassValue", valueOf(classIndirectLiteral).getContents());
        Assert.assertEquals("indirectClassValue", valueOf(classWarpingLiteral).getContents());

        final ConstantReference classResolvingFromProperty =
            (ConstantReference) PhpExpressionUtil.resolve(valueOf((PhpExpression) ((Field) getElementByName(fileSample, "resolvingFromProperty")).getDefaultValue()));

        Assert.assertEquals("TRUE", valueOf(classResolvingFromProperty).getText());

        final ConstantReference classResolvingDirectlyFromProperty =
            (ConstantReference) PhpExpressionUtil.resolve(valueOf((PhpExpression) ((Field) getElementByName(fileSample, "resolvingDirectlyFromProperty")).getDefaultValue()));

        Assert.assertEquals("null", valueOf(classResolvingDirectlyFromProperty).getText());

        // Avoiding complex loopings.
        final PhpExpression shouldAvoidCyclicLoopingsWithConstants  = (PhpExpression) getElementByName(fileSample, "shouldAvoidCyclicLoopingsWithConstants");
        final PhpExpression shouldAvoidCyclicLoopingsWithVariablesA = (PhpExpression) getElementByName(fileSample, "shouldAvoidCyclicLoopingsWithVariablesA");

        Assert.assertNull(PhpExpressionUtil.resolve(shouldAvoidCyclicLoopingsWithConstants));
        Assert.assertNull(PhpExpressionUtil.resolve(shouldAvoidCyclicLoopingsWithVariablesA));

        // Resolving variables.
        final StringLiteralExpression variableWrapping = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "variableWrapping"));

        Assert.assertEquals("value", valueOf(variableWrapping).getContents());

        // With wrapping parentheses.
        final StringLiteralExpression withParanteshesWrapping = (StringLiteralExpression) PhpExpressionUtil.resolve(getStringLiteral(fileSample, "withParanteshesWrapping"));

        Assert.assertEquals("parentheses", valueOf(withParanteshesWrapping).getContents());
    }
}
