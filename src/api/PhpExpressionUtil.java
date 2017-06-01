package net.rentalhost.idea.api;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;

import java.util.ArrayList;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PhpExpressionUtil {
    ;

    private static final Collection<String> primaryConstants = new ArrayList<>();

    static {
        primaryConstants.add("null");
        primaryConstants.add("true");
        primaryConstants.add("false");
    }

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

        if (isPrimaryConstant(referencedValue)) {
            return (PhpExpression) referencedValue;
        }

        return resolver.resolve((PhpReference) referencedValue);
    }

    private static boolean isPrimaryConstant(final PsiElement element) {
        return primaryConstants.contains(element.getText().toLowerCase());
    }

    @Nullable
    public static PhpExpression from(@NotNull final PhpExpression elementInitial) {
        return RecursionResolver.resolve(elementInitial, resolver -> {
            final PhpExpression element = (PhpExpression) resolver.getObject();

            if ((element instanceof ConstantReference) &&
                isPrimaryConstant(element)) {
                return element;
            }

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

            return element;
        });
    }
}
