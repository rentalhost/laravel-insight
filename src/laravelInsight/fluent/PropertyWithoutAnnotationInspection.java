package net.rentalhost.idea.laravelInsight.fluent;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.FieldReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.idea.laravelInsight.utils.PropertyQuickFix;
import net.rentalhost.idea.utils.PhpClassUtil;
import net.rentalhost.idea.utils.PhpDocCommentUtil;

public class PropertyWithoutAnnotationInspection extends PhpInspection {
    @NotNull private static final String messagePropertyUndefined = "Property was not annotated as @property $%s";

    @NotNull
    @Override
    public String getShortName() {
        return "PropertyWithoutAnnotationInspection";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(
        @NotNull final ProblemsHolder problemsHolder,
        final boolean isOnTheFly
    ) {
        return new PhpElementVisitor() {
            @Override
            public void visitPhpFieldReference(@NotNull final FieldReference fieldReference) {
                // Eg. is not like (new Fluent)->property;
                if (FluentUtil.isUsingIndirectly(fieldReference)) {
                    final List<PhpClass> fieldClasses = PhpClassUtil.resolve(fieldReference);
                    final PhpClass       fieldClass   = fieldClasses.get(0);
                    final String         fieldName    = fieldReference.getName();

                    assert fieldName != null;

                    if (fieldName.isEmpty()) {
                        return;
                    }

                    final Field fieldDeclaration = PhpClassUtil.findPropertyDeclaration(fieldClass, fieldName);

                    if ((fieldDeclaration != null) &&
                        fieldDeclaration.getModifier().isPublic()) {
                        return;
                    }

                    if (PhpDocCommentUtil.findPropertyRecursively(fieldClass, fieldName) == null) {
                        assert fieldReference.getNameNode() != null;

                        problemsHolder.registerProblem((PsiElement) fieldReference.getNameNode(),
                                                       String.format(messagePropertyUndefined, fieldName),
                                                       ProblemHighlightType.WEAK_WARNING,
                                                       new PropertyQuickFix(fieldClass, fieldName, "mixed"));
                    }
                }
            }
        };
    }
}
