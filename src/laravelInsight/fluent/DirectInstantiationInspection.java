package net.rentalhost.idea.laravelInsight.fluent;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.NewExpression;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;

import org.jetbrains.annotations.NotNull;

public class DirectInstantiationInspection extends PhpInspection {
    @NotNull private static final String messageDirectInstantiation = "Fluent should not be instantiated directly.";

    @NotNull
    @Override
    public String getShortName() {
        return "DirectInstantiationInspection";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(
        @NotNull final ProblemsHolder problemsHolder,
        final boolean isOnTheFly
    ) {
        return new PhpElementVisitor() {
            @Override
            public void visitPhpNewExpression(final NewExpression expression) {
                if (FluentUtil.isUsingDirectly(expression)) {
                    final ClassReference classReference = expression.getClassReference();
                    assert classReference != null;

                    problemsHolder.registerProblem(classReference, messageDirectInstantiation, ProblemHighlightType.WEAK_WARNING,
                                                   new TextRange(0, classReference.getTextLength()));
                }
            }
        };
    }
}
