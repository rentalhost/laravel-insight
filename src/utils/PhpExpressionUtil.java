package net.rentalhost.idea.utils;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.AssignmentExpression;
import com.jetbrains.php.lang.psi.elements.Constant;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.ParenthesizedExpression;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpReference;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;

import java.util.ArrayList;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PhpExpressionUtil {
    ;

    @NotNull private static final Collection<String> primaryConstants = new ArrayList<>();

    static {
        primaryConstants.add("null");
        primaryConstants.add("true");
        primaryConstants.add("false");
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

    @Nullable
    static PhpExpression recursionResolver(@NotNull final net.rentalhost.idea.utils.RecursionResolver.Resolver<PhpReference, PhpExpression> resolver) {
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
        else if (elementResolved instanceof Variable) {
            final PsiElement elementParent = elementResolved.getParent();

            if (elementParent instanceof AssignmentExpression) {
                referencedValue = ((AssignmentExpression) elementParent).getValue();
            }
        }

        if (!(referencedValue instanceof PhpReference)) {
            return (PhpExpression) referencedValue;
        }

        if (isPrimaryConstant(referencedValue)) {
            return (PhpExpression) referencedValue;
        }

        return resolver.resolve((PhpReference) referencedValue);
    }

    private static boolean isPrimaryConstant(@NotNull final PsiElement element) {
        return primaryConstants.contains(element.getText().toLowerCase());
    }
}
