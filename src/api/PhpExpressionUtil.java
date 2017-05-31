package net.rentalhost.idea.api;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PhpExpressionUtil {
    ;

    @Nullable
    static PhpExpression recursionResolver(final RecursionResolver.Resolver<PhpReference, PhpExpression> resolver) {
        final PsiElement elementResolved = resolver.getObject().resolve();

        if (elementResolved == null) {
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

        return resolver.resolve((PhpReference) referencedValue);
    }

    @Nullable
    public static PhpExpression from(@NotNull final PhpExpression elementInitial) {
        return RecursionResolver.resolve(elementInitial, resolver -> {
            final Object element = resolver.getObject();

            if (element instanceof PhpReference) {
                return RecursionResolver.resolve(element, PhpExpressionUtil::recursionResolver);
            }

            if (element instanceof AssignmentExpression) {
                final PhpPsiElement elementValue = ((AssignmentExpression) element).getValue();
                assert elementValue != null;

                if (elementValue instanceof PhpReference) {
                    return RecursionResolver.resolve(elementValue, PhpExpressionUtil::recursionResolver);
                }

                return (PhpExpression) resolver.resolve(elementValue);
            }

            return (PhpExpression) element;
        });
    }
}
