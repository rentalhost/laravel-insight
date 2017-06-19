package net.rentalhost.idea.laravelInsight.fluent;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.NewExpression;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import org.apache.commons.lang.StringUtils;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.idea.laravelInsight.resources.LaravelClasses;
import net.rentalhost.idea.utils.PhpClassUtil;

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
                final List<PhpClass> expressionClasses = PhpClassUtil.resolve(expression);

                if (expressionClasses.isEmpty()) {
                    return;
                }

                final PhpClass expressionClass    = expressionClasses.get(0);
                final String   expressionClassFQN = expressionClass.getFQN();
                final boolean isDirectInstance = expressionClassFQN.equals(LaravelClasses.SUPPORT_FLUENT.toString()) ||
                                                 expressionClassFQN.equals(LaravelClasses.SUPPORT_FLUENT_L54.toString());

                if (!isDirectInstance &&
                    (PhpClassUtil.findSuperOfType(expressionClass, LaravelClasses.SUPPORT_FLUENT.toString()) == null)) {
                    return;
                }

                // Case #1: new \Illuminate\Support\Fluent (directly) or
                //          new \Facades\Illuminate\Support\Fluent (facade, from Laravel 5.4);
                // Case #2: new \Fluent (facade);
                if (isDirectInstance ||
                    (StringUtils.countMatches(expressionClassFQN, "\\") == 1)) {
                    final ClassReference classReference = expression.getClassReference();
                    assert classReference != null;

                    problemsHolder.registerProblem(classReference, messageDirectInstantiation, ProblemHighlightType.WEAK_WARNING,
                                                   new TextRange(0, classReference.getTextLength()));
                }
            }
        };
    }
}
