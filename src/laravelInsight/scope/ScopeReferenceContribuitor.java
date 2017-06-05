package net.rentalhost.idea.laravelInsight.scope;

import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;

import org.jetbrains.annotations.NotNull;

public class ScopeReferenceContribuitor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull final PsiReferenceRegistrar registrar) {
        // Breakpoint here:
        registrar.toString();
    }
}
