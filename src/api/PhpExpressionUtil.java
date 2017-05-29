package net.rentalhost.idea.api;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;

import java.util.Stack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PhpExpressionUtil {
    ;

    @Nullable
    static PhpExpression recursionResolver(
        final PhpReference element,
        final Stack<PhpReference> stackedReferences
    ) {
        final PsiElement elementResolved = element.resolve();

        if (elementResolved == null) {
            return null;
        }

        if (stackedReferences.contains(element)) {
            return null;
        }

        PsiElement referencedValue = null;

        if (elementResolved instanceof ClassConstImpl) {
            referencedValue = ((Field) elementResolved).getDefaultValue();
        }
        else if (elementResolved instanceof Constant) {
            referencedValue = ((Constant) elementResolved).getValue();
        }

        if (!(referencedValue instanceof PhpReference)) {
            return (PhpExpression) referencedValue;
        }

        stackedReferences.push(element);

        return recursionResolver((PhpReference) referencedValue, stackedReferences);
    }

    @Nullable
    public static PhpExpression from(@NotNull final PhpExpression element) {
        if (element instanceof PhpReference) {
            return recursionResolver((PhpReference) element, new Stack<>());
        }

        if (element instanceof AssignmentExpression) {
            final PhpPsiElement elementValue = ((AssignmentExpression) element).getValue();
            assert elementValue != null;

            if (elementValue instanceof PhpReference) {
                return recursionResolver((PhpReference) elementValue, new Stack<>());
            }

            return from((PhpExpression) elementValue);
        }

        return element;
    }
}
