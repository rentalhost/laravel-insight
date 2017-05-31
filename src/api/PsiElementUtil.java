package net.rentalhost.idea.api;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ParenthesizedExpression;

import org.jetbrains.annotations.Nullable;

public enum PsiElementUtil {
    ;

    @Nullable
    public static <T extends PsiElement> T skipParentheses(final T elementInitial) {
        return RecursionResolver.resolve(elementInitial, resolver -> {
            final PsiElement element = (PsiElement) resolver.getObject();

            if (element instanceof ParenthesizedExpression) {
                return (T) resolver.resolve(((ParenthesizedExpression) element).getArgument());
            }

            return (T) element;
        });
    }
}
