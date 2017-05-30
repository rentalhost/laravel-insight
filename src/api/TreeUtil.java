package net.rentalhost.idea.api;

import com.intellij.psi.PsiElement;

import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

enum TreeUtil {
    ;

    @Nullable
    public static <T extends PsiElement> T getPrevMatch(
        final PsiElement element,
        final Function<T, Boolean> filterBy,
        final Function<T, Boolean> stopBy
    ) {
        PsiElement elementCurrent = element;

        while (elementCurrent != null) {
            if (filterBy.apply((T) elementCurrent)) {
                return (T) elementCurrent;
            }

            if (stopBy.apply((T) elementCurrent)) {
                break;
            }

            elementCurrent = elementCurrent.getPrevSibling();
        }

        return null;
    }
}
