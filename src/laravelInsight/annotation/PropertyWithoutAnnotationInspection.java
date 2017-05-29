package net.rentalhost.idea.laravelInsight.annotation;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;

import java.util.Collection;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.idea.api.PhpClassUtil;
import net.rentalhost.idea.api.PhpDocCommentUtil;
import net.rentalhost.idea.laravelInsight.resources.LaravelClasses;

public class PropertyWithoutAnnotationInspection extends PhpInspection {
    private static final String messagePropertyUndefined = "@property $%s was not annotated";

    @NotNull
    @Override
    public String getShortName() {
        return "PropertyWithoutAnnotationInspection";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(
        @NotNull final ProblemsHolder problemsHolder,
        final boolean b
    ) {
        return new ElementVisitor(problemsHolder);
    }

    private static class ElementVisitor extends PhpElementVisitor {
        private final ProblemsHolder problemsHolder;

        ElementVisitor(final ProblemsHolder problemsHolder) {
            this.problemsHolder = problemsHolder;
        }

        @Override
        public void visitPhpField(final Field field) {
            if (!Objects.equals(field.getName(), "casts")) {
                return;
            }

            if (!PhpType.intersects(field.getType(), PhpType.ARRAY)) {
                return;
            }

            final PhpClass fieldClass = field.getContainingClass();

            assert fieldClass != null;

            if (!PhpClassUtil.hasSuperOfType(fieldClass, LaravelClasses.ELOQUENT_MODEL.toString())) {
                return;
            }

            final PhpDocComment classDocComment = fieldClass.getDocComment();

            final PsiElement                   fieldValue  = field.getDefaultValue();
            final Collection<ArrayHashElement> fieldHashes = PsiTreeUtil.findChildrenOfType(fieldValue, ArrayHashElement.class);

            for (final ArrayHashElement fieldHash : fieldHashes) {
                if (!(fieldHash.getValue() instanceof StringLiteralExpression)) {
                    continue;
                }

                final PhpPsiElement hashKey = fieldHash.getKey();

                assert hashKey != null;

                if (!(hashKey instanceof StringLiteralExpression)) {
                    continue;
                }

                final String hashKeyContents = ((StringLiteralExpression) hashKey).getContents();

                if (classDocComment == null) {
                    registerPropertyUndefined(hashKey, hashKeyContents);
                    continue;
                }

                final boolean isNotAnnotated = !PhpDocCommentUtil.hasProperty(classDocComment, hashKeyContents);

                if (isNotAnnotated) {
                    registerPropertyUndefined(hashKey, hashKeyContents);
                }
            }
        }

        private void registerPropertyUndefined(
            final PsiElement hashKey,
            final String hashKeyContents
        ) {
            problemsHolder.registerProblem(hashKey,
                                           String.format(messagePropertyUndefined, hashKeyContents),
                                           ProblemHighlightType.WEAK_WARNING);
        }
    }
}
