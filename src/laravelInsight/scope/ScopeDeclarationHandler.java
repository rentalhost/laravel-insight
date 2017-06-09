package net.rentalhost.idea.laravelInsight.scope;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.MemberReference;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;

import java.util.Collection;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import net.rentalhost.idea.laravelInsight.resources.LaravelClasses;
import net.rentalhost.idea.utils.PhpClassUtil;
import net.rentalhost.idea.utils.PsiElementUtil;

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

        final PhpExpression sourceReference = ((MemberReference) sourceParent).getClassReference();
        assert sourceReference != null;

        final PsiElement sourceReferenceDirect = PsiElementUtil.skipParentheses(sourceReference);
        assert sourceReferenceDirect != null;

        final Project     sourceProject        = sourceElement.getProject();
        final String      sourceResolution     = "scope" + sourceElement.getText();
        final Set<String> sourceReferenceTypes = ((PhpTypedElement) sourceReferenceDirect).getType().global(sourceProject).getTypes();

        for (final String sourceReferenceType : sourceReferenceTypes) {
            final Collection<PhpClass> referenceClasses = PhpIndex.getInstance(sourceProject).getAnyByFQN(sourceReferenceType);

            if (referenceClasses.isEmpty()) {
                continue;
            }

            final PhpClass referenceClass = referenceClasses.iterator().next();

            if (PhpClassUtil.findSuperOfType(referenceClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                continue;
            }

            final Method methodDeclaration = PhpClassUtil.findMethodDeclaration(referenceClass, sourceResolution);

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
