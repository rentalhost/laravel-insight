package net.rentalhost.idea.laravelInsight.fluent;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpReturnType;
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
                final PhpReturnType  returnType                     = method.getReturnType();
                final ClassReference methodReturnTypeClassReference = (returnType != null) ? returnType.getClassReference() : null;

                if (FluentUtil.isUsingDirectly(methodReturnTypeClassReference)) {
                    problemsHolder.registerProblem(methodReturnTypeClassReference, messageDirectInstantiation, ProblemHighlightType.WEAK_WARNING);
                }
            }

            @Override
            public void visitPhpFunction(final Function function) {
                final PhpReturnType functionReturnType = function.getReturnType();
                if ((functionReturnType != null) && FluentUtil.isUsingDirectly(functionReturnType.getClassReference())) {
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
