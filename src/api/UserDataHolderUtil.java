package net.rentalhost.idea.api;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

enum UserDataHolderUtil {
    ;

    @NotNull
    public static <OutputType, SupplierType> OutputType from(
        @NotNull final PsiElement element,
        @NotNull final Key key,
        @NotNull final Supplier<SupplierType> keySupplier
    ) {
        final OutputType elementData = (OutputType) element.getUserData(key);

        if (elementData == null) {
            final SupplierType newSupplier = keySupplier.get();

            element.putUserData(key, newSupplier);

            return (OutputType) newSupplier;
        }

        return elementData;
    }
}
