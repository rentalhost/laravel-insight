package net.rentalhost.idea.laravelInsight.annotation;

import com.google.common.base.CaseFormat;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.idea.api.*;
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

        @NotNull
        private static PsiElement getReportableElement(
            final PsiNameIdentifierOwner phpClass,
            final PhpClassMember fieldPrimaryKey
        ) {
            final PsiElement issueReceptor;

            if (Objects.equals(fieldPrimaryKey.getContainingClass(), phpClass)) {
                issueReceptor = fieldPrimaryKey.getNameIdentifier();
                assert issueReceptor != null;
            }
            else {
                issueReceptor = phpClass.getNameIdentifier();
                assert issueReceptor != null;
            }

            return issueReceptor;
        }

        private static boolean isRelationship(final Collection<String> functionTypes) {
            return functionTypes.contains(LaravelClasses.ELOQUENT_RELATIONSHIP_HASONE.toString()) ||
                   functionTypes.contains(LaravelClasses.ELOQUENT_RELATIONSHIP_HASMANY.toString()) ||
                   functionTypes.contains(LaravelClasses.ELOQUENT_RELATIONSHIP_HASMANYTHROUGHT.toString()) ||
                   functionTypes.contains(LaravelClasses.ELOQUENT_RELATIONSHIP_MORPHTO.toString()) ||
                   functionTypes.contains(LaravelClasses.ELOQUENT_RELATIONSHIP_MORPHONE.toString()) ||
                   functionTypes.contains(LaravelClasses.ELOQUENT_RELATIONSHIP_MORPHMANY.toString()) ||
                   functionTypes.contains(LaravelClasses.ELOQUENT_RELATIONSHIP_MORPHTOMANY.toString()) ||
                   functionTypes.contains(LaravelClasses.ELOQUENT_RELATIONSHIP_BELONGSTO.toString()) ||
                   functionTypes.contains(LaravelClasses.ELOQUENT_RELATIONSHIP_BELONGSTOMANY.toString());
        }

        @Override
        public void visitPhpField(final Field field) {
            final String fieldName = field.getName();

            if (!Objects.equals(fieldName, "casts") &&
                !Objects.equals(fieldName, "dates")) {
                return;
            }

            if (!PhpType.intersects(field.getType(), PhpType.ARRAY)) {
                return;
            }

            final PhpClass fieldClass = field.getContainingClass();
            assert fieldClass != null;

            if (PhpClassUtil.findSuperOfType(fieldClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                return;
            }

            final PsiElement fieldValue = field.getDefaultValue();

            if (!(fieldValue instanceof ArrayCreationExpression)) {
                return;
            }

            final Iterable<ArrayHashElement> fieldHashes = ((ArrayCreationExpression) fieldValue).getHashElements();

            for (final ArrayHashElement fieldHash : fieldHashes) {
                final PhpPsiElement fieldHashValue = fieldHash.getValue();
                assert fieldHashValue != null;

                final PhpExpression fieldHashResolvedValue = PhpExpressionUtil.from((PhpExpression) fieldHashValue);

                if (!(fieldHashResolvedValue instanceof StringLiteralExpression)) {
                    continue;
                }

                final PhpPsiElement hashKey = fieldHash.getKey();
                assert hashKey != null;

                final PhpExpression hashKeyResolvedValue = PhpExpressionUtil.from((PhpExpression) hashKey);

                if (!(hashKeyResolvedValue instanceof StringLiteralExpression)) {
                    continue;
                }

                final String hashKeyContents = ((StringLiteralExpression) hashKeyResolvedValue).getContents();

                validatePropertyAnnotation(fieldClass, hashKey, hashKeyContents);
            }
        }

        @Override
        public void visitPhpUse(final PhpUse expression) {
            if (expression.isTraitImport()) {
                final PhpReference traitReferenceClass = expression.getTargetReference();
                assert traitReferenceClass != null;

                final PhpClass traitContainingClass = PhpClassUtil.getTraitContainingClass(expression);
                assert traitContainingClass != null;

                if (PhpClassUtil.findSuperOfType(traitContainingClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                    return;
                }

                if (Objects.equals(traitReferenceClass.getFQN(), LaravelClasses.ELOQUENT_SOFTDELETES_TRAIT.toString())) {
                    validatePropertyAnnotation(traitContainingClass, expression, "deleted_at");
                    return;
                }

                final PhpClass traitResolvedClass = (PhpClass) traitReferenceClass.resolve();

                if (traitResolvedClass == null) {
                    return;
                }

                if (PhpClassUtil.findTraitOfType(traitResolvedClass, LaravelClasses.ELOQUENT_SOFTDELETES_TRAIT.toString()) == null) {
                    return;
                }

                validatePropertyAnnotation(traitResolvedClass, expression, "deleted_at");
            }
        }

        @Override
        public void visitPhpClass(final PhpClass phpClass) {
            if (PhpClassUtil.findSuperOfType(phpClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                return;
            }

            reportTimestamps(phpClass);
            reportPrimaryKey(phpClass);
        }

        @Override
        public void visitPhpMethod(final Method method) {
            final PhpClass methodClass = method.getContainingClass();
            assert methodClass != null;

            if (PhpClassUtil.findSuperOfType(methodClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                return;
            }

            final String methodName = method.getName();

            if (methodName.endsWith("Attribute")) {
                if (methodName.startsWith("get") ||
                    methodName.startsWith("set")) {
                    final PsiElement methodIdentifier = method.getNameIdentifier();
                    assert methodIdentifier != null;

                    final String methodPropertyPart = methodName.substring(3, methodName.length() - 9);

                    validatePropertyAnnotation(methodClass, methodIdentifier, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, methodPropertyPart));
                }
            }

            reportRelationship(method, methodClass);
        }

        @Override
        public void visitPhpFieldReference(final FieldReference fieldReference) {
            final ASTNode fieldNameNode = fieldReference.getNameNode();

            if (fieldNameNode == null) {
                return;
            }

            final String fieldNameText = fieldNameNode.getText();

            if (!Objects.equals(fieldNameText, fieldNameText.toLowerCase())) {
                return;
            }

            final PsiElement fieldClassReferenceRaw = fieldReference.getClassReference();
            assert fieldClassReferenceRaw != null;

            final PsiElement fieldClassReference = PsiElementUtil.skipParentheses(fieldClassReferenceRaw);

            if (!(fieldClassReference instanceof PhpTypedElement)) {
                return;
            }

            final Set<String> fieldClassReferenceTypes = ((PhpTypedElement) fieldClassReference).getType().global(problemsHolder.getProject()).getTypes();

            for (final String fieldClassType : fieldClassReferenceTypes) {
                final Collection<PhpClass> fieldClasses = PhpIndex.getInstance(problemsHolder.getProject()).getAnyByFQN(fieldClassType);

                if (fieldClasses.isEmpty()) {
                    continue;
                }

                final PhpClass fieldClass = fieldClasses.iterator().next();

                if (PhpClassUtil.findSuperOfType(fieldClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                    continue;
                }

                final String fieldName = fieldReference.getName();
                assert fieldName != null;

                final Field fieldDeclaration = PhpClassUtil.findPropertyDeclaration(fieldClass, fieldName);

                if ((fieldDeclaration != null) &&
                    fieldDeclaration.getModifier().isPublic()) {
                    continue;
                }

                validatePropertyAnnotation(fieldClass, fieldNameNode.getPsi(), fieldNameText);
                break;
            }
        }

        private void reportRelationship(
            final Function method,
            final PhpClass methodClass
        ) {
            final PhpType methodReturnType = PhpFunctionUtil.getReturnType(method);

            if (!isRelationship(methodReturnType.getTypes())) {
                return;
            }

            final PsiElement methodIdentifier = method.getNameIdentifier();
            assert methodIdentifier != null;

            validatePropertyAnnotation(methodClass, methodIdentifier, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, method.getName()));
        }

        private void reportTimestamps(final PhpClass phpClass) {
            final Field fieldTimestamps = PhpClassUtil.findPropertyDeclaration(phpClass, "timestamps");

            if (fieldTimestamps == null) {
                return;
            }

            final PsiElement fieldTimestampsDefaultValue = fieldTimestamps.getDefaultValue();

            if (!(fieldTimestampsDefaultValue instanceof ConstantReference)) {
                return;
            }

            if (!"true".equals(fieldTimestampsDefaultValue.getText())) {
                return;
            }

            final PsiElement issueReceptor = getReportableElement(phpClass, fieldTimestamps);

            validatePropertyAnnotation(phpClass, issueReceptor, "created_at");
            validatePropertyAnnotation(phpClass, issueReceptor, "updated_at");
        }

        private void reportPrimaryKey(final PhpClass phpClass) {
            final Field fieldPrimaryKey = PhpClassUtil.findPropertyDeclaration(phpClass, "primaryKey");

            if (fieldPrimaryKey == null) {
                return;
            }

            final PsiElement fieldPrimaryKeyValue = fieldPrimaryKey.getDefaultValue();

            if (!(fieldPrimaryKeyValue instanceof PhpExpression)) {
                return;
            }

            final PhpExpression fieldPrimaryKeyValueResolved = PhpExpressionUtil.from((PhpExpression) fieldPrimaryKeyValue);

            if (!(fieldPrimaryKeyValueResolved instanceof StringLiteralExpression)) {
                return;
            }

            final PsiElement issueReceptor = getReportableElement(phpClass, fieldPrimaryKey);

            validatePropertyAnnotation(phpClass, issueReceptor, ((StringLiteralExpression) fieldPrimaryKeyValueResolved).getContents());
        }

        private void validatePropertyAnnotation(
            final PhpClass phpClass,
            final PsiElement issueReference,
            final String propertyName
        ) {
            PhpClass fieldClassCurrent = phpClass;
            boolean  isNotAnnotated    = true;

            while (fieldClassCurrent != null) {
                final PhpDocComment classDocComment = fieldClassCurrent.getDocComment();

                if (classDocComment != null) {
                    if (PhpDocCommentUtil.findProperty(classDocComment, propertyName) != null) {
                        isNotAnnotated = false;
                        break;
                    }
                }

                fieldClassCurrent = PhpClassUtil.getSuper(fieldClassCurrent);
            }

            if (isNotAnnotated) {
                registerPropertyUndefined(issueReference, propertyName);
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
