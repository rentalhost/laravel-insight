package net.rentalhost.idea.utils;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ParenthesizedExpression;

import org.jetbrains.annotations.Nullable;

public enum PsiElementUtil {
    ;

    @Nullable
    public static <T extends PsiElement> T skipParentheses(final T elementInitial) {
        if (elementInitial instanceof ParenthesizedExpression) {
            return (T) ((ParenthesizedExpression) elementInitial).unparenthesize();
        }

        return elementInitial;
    }
}
