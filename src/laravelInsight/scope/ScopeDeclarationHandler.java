package net.rentalhost.idea.laravelInsight.scope;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.rentalhost.idea.laravelInsight.resources.LaravelClasses;
import net.rentalhost.idea.utils.PhpClassUtil;

public class ScopeDeclarationHandler implements GotoDeclarationHandler {
    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(
        @Nullable final PsiElement sourceElement,
        final int offset,
        final Editor editor
    ) {
        if (sourceElement == null) {
            return null;
        }

        final PsiElement sourceParent = sourceElement.getParent();

        if (!(sourceParent instanceof MethodReference)) {
            return null;
        }

        final List<PhpClass> sourceClasses = PhpClassUtil.resolve(sourceParent);

        if (sourceClasses.isEmpty()) {
            return null;
        }

        final String sourceResolution = "scope" + sourceElement.getText();

        for (final PhpClass sourceClass : sourceClasses) {
            if (PhpClassUtil.findSuperOfType(sourceClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                continue;
            }

            final Method methodDeclaration = PhpClassUtil.findMethodDeclaration(sourceClass, sourceResolution);

            if (methodDeclaration == null) {
                continue;
            }

            return new PsiElement[] { methodDeclaration };
        }

        return null;
    }

    @Nullable
    @Override
    public String getActionText(final DataContext context) {
        return "Go to scope declaration";
    }
}
