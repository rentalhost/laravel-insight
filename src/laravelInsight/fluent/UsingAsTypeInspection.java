package net.rentalhost.idea.laravelInsight.fluent;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;

import org.jetbrains.annotations.NotNull;

public class UsingAsTypeInspection extends PhpInspection {
    @NotNull private static final String messageDirectInstantiation = "Fluent should not be used as type.";

    @NotNull
    @Override
    public String getShortName() {
        return "UsingAsTypeInspection";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(
        @NotNull final ProblemsHolder problemsHolder,
        final boolean isOnTheFly
    ) {
        return new PhpElementVisitor() {
            @Override
            public void visitPhpMethod(final Method method) {
                final PsiElement methodReturnType = method.getReturnType();

                if (FluentUtil.isUsingDirectly(methodReturnType)) {
                    problemsHolder.registerProblem(methodReturnType, messageDirectInstantiation, ProblemHighlightType.WEAK_WARNING);
                }
            }

            @Override
            public void visitPhpFunction(final Function function) {
                final PsiElement functionReturnType = function.getReturnType();

                if (FluentUtil.isUsingDirectly(functionReturnType)) {
                    problemsHolder.registerProblem(functionReturnType, messageDirectInstantiation, ProblemHighlightType.WEAK_WARNING);
                }
            }

            @Override
            public void visitPhpParameter(final Parameter parameter) {
                if (FluentUtil.isUsingDirectly(parameter)) {
                    problemsHolder.registerProblem(parameter, messageDirectInstantiation, ProblemHighlightType.WEAK_WARNING);
                }
            }

            @Override
            public void visitPhpDocType(final PhpDocType type) {
                if (FluentUtil.isUsingDirectly(type)) {
                    problemsHolder.registerProblem(type, messageDirectInstantiation, ProblemHighlightType.WEAK_WARNING);
                }
            }
        };
    }
}
