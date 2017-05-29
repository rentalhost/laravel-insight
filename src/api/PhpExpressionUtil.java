package net.rentalhost.idea.api;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PhpExpressionUtil {
    ;

    @Nullable
    public static PhpExpression from(@NotNull final PhpExpression element) {
        if (element instanceof PhpReference) {
            final PsiElement constantResolved = ((PsiReference) element).resolve();

            if (constantResolved == null) {
                return null;
            }

            if (constantResolved instanceof ClassConstImpl) {
                final PsiElement constantResolvedValue = ((Field) constantResolved).getDefaultValue();
                assert constantResolvedValue != null;

                return from((PhpExpression) constantResolvedValue);
            }

            if (constantResolved instanceof Constant) {
                final PsiElement constantResolvedValue = ((Constant) constantResolved).getValue();
                assert constantResolvedValue != null;

                return from((PhpExpression) constantResolvedValue);
            }
        }

        if (element instanceof AssignmentExpression) {
            final PhpPsiElement elementValue = ((AssignmentExpression) element).getValue();
            assert elementValue != null;

            return from((PhpExpression) elementValue);
        }

        return element;
    }
}
