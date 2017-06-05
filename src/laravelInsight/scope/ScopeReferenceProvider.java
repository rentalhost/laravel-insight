package net.rentalhost.idea.laravelInsight.scope;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

public class ScopeReferenceProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(
        @NotNull final PsiElement element,
        @NotNull final ProcessingContext context
    ) {
        // Breakpoint here:
        return new PsiReference[0];
    }
}
