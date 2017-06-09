package net.rentalhost.idea.laravelInsight.scope;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpReference;
import org.junit.Assert;

import java.util.Arrays;

import net.rentalhost.suite.FixtureSuite;

public class ScopeDeclarationHandlerTest extends FixtureSuite {
    private static ASTNode getNameIdentifier(
        final PsiElement fileSample,
        final String nameIdentifier
    ) {
        return valueOf(((PhpReference) getElementAssignmentValueByName(fileSample, nameIdentifier)).getNameNode());
    }

    public void testDeclarationHandler() {
        final GotoDeclarationHandler scopeDeclarationHandler = new ScopeDeclarationHandler();
        final PsiFile                fileSample              = getResourceFile("laravelInsight/scope/ScopeDeclarationHandler.samples.php");
        final PhpNamedElement        scopeTestReference      = getElementByName(fileSample, "scopeTestReference");

        final ASTNode shouldBeResolvable = getNameIdentifier(fileSample, "shouldBeResolvable");
        Assert.assertSame(scopeTestReference, Arrays.asList(valueOf(scopeDeclarationHandler.getGotoDeclarationTargets((PsiElement) shouldBeResolvable, 0, getEditor()))).get(0));

        // Code-coverage.
        Assert.assertNull(scopeDeclarationHandler.getGotoDeclarationTargets(null, 0, getEditor()));

        final PhpNamedElement ccSimpleReference = getElementByName(fileSample, "ccSimpleReference");
        Assert.assertNull(scopeDeclarationHandler.getGotoDeclarationTargets(ccSimpleReference, 0, getEditor()));

        final ASTNode ccIsNotAScopedMethod = getNameIdentifier(fileSample, "ccIsNotAScopedMethod");
        Assert.assertNull(scopeDeclarationHandler.getGotoDeclarationTargets((PsiElement) ccIsNotAScopedMethod, 0, getEditor()));

        final ASTNode ccUnresolvableClass = getNameIdentifier(fileSample, "ccUnresolvableClass");
        Assert.assertNull(scopeDeclarationHandler.getGotoDeclarationTargets((PsiElement) ccUnresolvableClass, 0, getEditor()));

        final ASTNode ccNotAModel = getNameIdentifier(fileSample, "ccNotAModel");
        Assert.assertNull(scopeDeclarationHandler.getGotoDeclarationTargets((PsiElement) ccNotAModel, 0, getEditor()));

        final ASTNode ccIsNotAScopedMethodOnModel = getNameIdentifier(fileSample, "ccIsNotAScopedMethodOnModel");
        Assert.assertNull(scopeDeclarationHandler.getGotoDeclarationTargets((PsiElement) ccIsNotAScopedMethodOnModel, 0, getEditor()));

        Assert.assertNotNull(scopeDeclarationHandler.getActionText(DataContext.EMPTY_CONTEXT));
    }
}
