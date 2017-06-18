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

public enum PhpExpressionUtil {
    ;

    @NotNull private static final Collection<String> primaryConstants = new ArrayList<>();

    static {
        primaryConstants.add("null");
        primaryConstants.add("true");
        primaryConstants.add("false");
    }

    @NotNull
    public static PhpExpression resolve(@NotNull final PhpExpression elementInitial) {
        final PhpExpression elementResolution = RecursionResolver.resolve(elementInitial, resolver -> {
            PhpExpression element = (PhpExpression) resolver.getObject();

            if (element instanceof ParenthesizedExpression) {
                element = (PhpExpression) ((ParenthesizedExpression) element).unparenthesize();
            }

            if ((element instanceof ConstantReference) &&
                isPrimaryConstant(element)) {
                return element;
            }

            if (element instanceof PhpReference) {
                return (PhpExpression) RecursionResolver.resolve(element, PhpExpressionUtil::recursionResolver);
            }

            if (element instanceof AssignmentExpression) {
                final PhpPsiElement elementValue = ((AssignmentExpression) element).getValue();
                assert elementValue != null;

                if (elementValue instanceof PhpReference) {
                    return (PhpExpression) RecursionResolver.resolve(elementValue, PhpExpressionUtil::recursionResolver);
                }

                return (PhpExpression) resolver.resolve(elementValue);
            }

            return element;
        });
        assert elementResolution != null;

        return elementResolution;
    }

    @NotNull
    static PsiElement recursionResolver(@NotNull final RecursionResolver.Resolver<PhpReference, PhpExpression> resolver) {
        final PhpReference element         = resolver.getObject();
        final PsiElement   elementResolved = element.resolve();

        if (elementResolved == null) {
            return element;
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

        if (referencedValue == null) {
            return element;
        }

        if (referencedValue instanceof ParenthesizedExpression) {
            referencedValue = ((ParenthesizedExpression) referencedValue).unparenthesize();
        }

        if (!(referencedValue instanceof PhpReference) ||
            isPrimaryConstant(referencedValue)) {
            return referencedValue;
        }

        final PhpExpression elementResolution = resolver.resolve((PhpReference) referencedValue);

        if (elementResolution == null) {
            return element;
        }

        return elementResolution;
    }

    private static boolean isPrimaryConstant(@NotNull final PsiElement element) {
        return primaryConstants.contains(element.getText().toLowerCase());
    }
}
