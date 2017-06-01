package net.rentalhost.idea.laravelInsight.annotation;

import com.google.common.base.CaseFormat;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocProperty;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import net.rentalhost.idea.api.*;
import net.rentalhost.idea.laravelInsight.resources.LaravelClasses;

public class ColumnWithoutAnnotationInspection extends PhpInspection {
    private static final String messagePropertyUndefined = "@property $%s was not annotated";

    private enum InspectionHelper {
        ;

        static void registerPropertyUndefined(
            @NotNull final ProblemsHolder problemsHolder,
            final PhpClass primaryClass,
            final PsiElement issuedElement,
            final String propertyName,
            final String propertyType
        ) {
            problemsHolder.registerProblem(issuedElement,
                                           String.format(messagePropertyUndefined, propertyName),
                                           ProblemHighlightType.WEAK_WARNING,
                                           new InspectionQuickFix(primaryClass, propertyName, propertyType));
        }

        static void validatePropertyAnnotation(
            @NotNull final ProblemsHolder problemsHolder,
            final PhpClass phpClass,
            final PsiElement issueReference,
            final String propertyName,
            final String propertyType
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
                registerPropertyUndefined(problemsHolder, phpClass, issueReference, propertyName, propertyType);
            }
        }

        static void validatePropertyAnnotation(
            @NotNull final ProblemsHolder problemsHolder,
            final PhpClass phpClass,
            final PsiElement issueReference,
            final String propertyName
        ) {
            if (propertyName.endsWith("_id")) {
                validatePropertyAnnotation(problemsHolder, phpClass, issueReference, propertyName, "int");
                return;
            }

            if (propertyName.endsWith("_at")) {
                validatePropertyAnnotation(problemsHolder, phpClass, issueReference, propertyName, "\\Carbon\\Carbon");
                return;
            }

            validatePropertyAnnotation(problemsHolder, phpClass, issueReference, propertyName, "mixed");
        }

        static void reportTimestamps(
            @NotNull final ProblemsHolder problemsHolder,
            final PhpClass phpClass
        ) {
            final Field fieldTimestamps = PhpClassUtil.findPropertyDeclaration(phpClass, "timestamps");

            if (fieldTimestamps == null) {
                return;
            }

            final PsiElement fieldTimestampsDefaultValue = fieldTimestamps.getDefaultValue();

            if (!(fieldTimestampsDefaultValue instanceof PhpExpression)) {
                return;
            }

            final PsiElement fieldTimestampsDefaultValueResolved = PhpExpressionUtil.from((PhpExpression) fieldTimestampsDefaultValue);

            if (!(fieldTimestampsDefaultValueResolved instanceof ConstantReference)) {
                return;
            }

            if (!"true".equals(fieldTimestampsDefaultValueResolved.getText())) {
                return;
            }

            final PsiElement issueReceptor = getReportableElement(phpClass, fieldTimestamps);

            validatePropertyAnnotation(problemsHolder, phpClass, issueReceptor, "created_at");
            validatePropertyAnnotation(problemsHolder, phpClass, issueReceptor, "updated_at");
        }

        static void reportPrimaryKey(
            @NotNull final ProblemsHolder problemsHolder,
            final PhpClass phpClass
        ) {
            final PsiElement issueReceptor;
            final Field      fieldPrimaryKey              = PhpClassUtil.findPropertyDeclaration(phpClass, "primaryKey");
            String           fieldPrimaryKeyResolvedValue = "id";

            if (fieldPrimaryKey != null) {
                final PsiElement fieldPrimaryKeyValue = fieldPrimaryKey.getDefaultValue();

                if (!(fieldPrimaryKeyValue instanceof PhpExpression)) {
                    return;
                }

                final PhpExpression fieldPrimaryKeyResolved = PhpExpressionUtil.from((PhpExpression) fieldPrimaryKeyValue);

                if (!(fieldPrimaryKeyResolved instanceof StringLiteralExpression)) {
                    return;
                }

                fieldPrimaryKeyResolvedValue = ((StringLiteralExpression) fieldPrimaryKeyResolved).getContents();
                issueReceptor = getReportableElement(phpClass, fieldPrimaryKey);
            }
            else {
                issueReceptor = phpClass.getNameIdentifier();
                assert issueReceptor != null;
            }

            final Field fieldKeyType = PhpClassUtil.findPropertyDeclaration(phpClass, "keyType");
            String      fieldType    = "int";

            if (fieldKeyType != null) {
                final PhpExpression fieldKeyTypeValueRaw = (PhpExpression) fieldKeyType.getDefaultValue();

                if (fieldKeyTypeValueRaw != null) {
                    final PsiElement fieldKeyTypeValue = PhpExpressionUtil.from(fieldKeyTypeValueRaw);

                    if (fieldKeyTypeValue instanceof StringLiteralExpression) {
                        fieldType = ((StringLiteralExpression) fieldKeyTypeValue).getContents();
                    }
                }
            }

            validatePropertyAnnotation(problemsHolder, phpClass, issueReceptor, fieldPrimaryKeyResolvedValue, fieldType);
        }

        static void reportAccessorOrMutator(
            @NotNull final ProblemsHolder problemsHolder,
            final PsiNameIdentifierOwner method,
            final PhpClass methodClass,
            final String methodName
        ) {
            if (methodName.endsWith("Attribute")) {
                final boolean isAccessor = methodName.startsWith("get");

                if (isAccessor ||
                    methodName.startsWith("set")) {
                    final PsiElement methodIdentifier = method.getNameIdentifier();
                    assert methodIdentifier != null;

                    String methodReturnType = "mixed";

                    if (isAccessor) {
                        methodReturnType = PhpFunctionUtil.getReturnType((Function) method).toString();
                    }

                    final String methodPropertyPart = methodName.substring(3, methodName.length() - 9);

                    validatePropertyAnnotation(problemsHolder, methodClass, methodIdentifier,
                                               CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, methodPropertyPart), methodReturnType);
                }
            }
        }

        static void reportRelationship(
            @NotNull final ProblemsHolder problemsHolder,
            final Function method,
            final PhpClass methodClass
        ) {
            final PhpType methodReturnType = PhpFunctionUtil.getReturnType(method);

            if (!isRelationship(methodReturnType.getTypes())) {
                return;
            }

            final PsiElement methodIdentifier = method.getNameIdentifier();
            assert methodIdentifier != null;

            validatePropertyAnnotation(problemsHolder, methodClass, methodIdentifier, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, method.getName()), "mixed");
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
    }

    @NotNull
    @Override
    public String getShortName() {
        return "ColumnWithoutAnnotationInspection";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(
        @NotNull final ProblemsHolder problemsHolder,
        final boolean b
    ) {
        return new PhpElementVisitor() {
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

                    InspectionHelper.validatePropertyAnnotation(problemsHolder, fieldClass, hashKey, hashKeyContents);
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
                        InspectionHelper.validatePropertyAnnotation(problemsHolder, traitContainingClass, expression, "deleted_at");
                        return;
                    }

                    final PhpClass traitResolvedClass = (PhpClass) traitReferenceClass.resolve();

                    if (traitResolvedClass == null) {
                        return;
                    }

                    if (PhpClassUtil.findTraitOfType(traitResolvedClass, LaravelClasses.ELOQUENT_SOFTDELETES_TRAIT.toString()) == null) {
                        return;
                    }

                    InspectionHelper.validatePropertyAnnotation(problemsHolder, traitContainingClass, expression, "deleted_at");
                }
            }

            @Override
            public void visitPhpClass(final PhpClass phpClass) {
                if (PhpClassUtil.findSuperOfType(phpClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                    return;
                }

                InspectionHelper.reportTimestamps(problemsHolder, phpClass);
                InspectionHelper.reportPrimaryKey(problemsHolder, phpClass);
            }

            @Override
            public void visitPhpMethod(final Method method) {
                final PhpClass methodClass = method.getContainingClass();
                assert methodClass != null;

                if (PhpClassUtil.findSuperOfType(methodClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                    return;
                }

                final String methodName = method.getName();

                InspectionHelper.reportAccessorOrMutator(problemsHolder, method, methodClass, methodName);
                InspectionHelper.reportRelationship(problemsHolder, method, methodClass);
            }

            @Override
            public void visitPhpFieldReference(final FieldReference fieldReference) {
                if (fieldReference.isStatic()) {
                    return;
                }

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
                assert fieldClassReference != null;

                if (fieldClassReference instanceof ParenthesizedExpression) {
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

                    final Field fieldDeclaration = PhpClassUtil.findPropertyDeclaration(fieldClass, fieldNameText);

                    if ((fieldDeclaration != null) &&
                        fieldDeclaration.getModifier().isPublic()) {
                        continue;
                    }

                    InspectionHelper.validatePropertyAnnotation(problemsHolder, fieldClass, fieldNameNode.getPsi(), fieldNameText);
                    break;
                }
            }
        };
    }

    static class InspectionQuickFix implements LocalQuickFix {
        private final SmartPsiElementPointer<PhpClass> primaryClassPointer;
        private final String                           propertyName;
        private final String                           propertyType;
        private final String                           familyName;

        InspectionQuickFix(
            final PhpClass primaryClass,
            final String propertyName,
            final String propertyType
        ) {
            final SmartPointerManager pointerManager = SmartPointerManager.getInstance(primaryClass.getProject());

            primaryClassPointer = pointerManager.createSmartPsiElementPointer(primaryClass);
            this.propertyName = propertyName;
            this.propertyType = propertyType;
            familyName = String.format("Declare @property $%s on %s class", propertyName, primaryClass.getName());
        }

        @Nls
        @NotNull
        @Override
        public String getFamilyName() {
            return familyName;
        }

        @Override
        public void applyFix(
            @NotNull final Project project,
            @NotNull final ProblemDescriptor descriptor
        ) {
            final PhpClass primaryClass = primaryClassPointer.getElement();
            assert primaryClass != null;

            final PhpDocComment primaryClassDocComment = primaryClass.getDocComment();

            if (primaryClassDocComment != null) {
                final PhpDocProperty primaryClassProperty = PhpDocCommentUtil.findProperty(primaryClassDocComment, propertyName);

                if (!Objects.equals(primaryClassProperty, null)) {
                    return;
                }
            }

            final PhpDocTag  docCommentNewTag    = PhpDocCommentUtil.createTag(primaryClass, "@property", propertyType + " $" + propertyName);
            final PsiElement docCommentReference = docCommentNewTag.getParent();

            final Navigatable navigator = PsiNavigationSupport.getInstance().getDescriptor(docCommentReference.getNavigationElement());
            if (navigator != null) {
                navigator.navigate(true);

                final Editor selectedTextEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
                if (selectedTextEditor != null) {
                    final int startOffset = docCommentNewTag.getTextOffset() + 10;
                    final int endOffset   = startOffset + propertyType.length();

                    selectedTextEditor.getSelectionModel().setSelection(startOffset, endOffset);
                    selectedTextEditor.getCaretModel().moveToOffset(endOffset);
                }
            }
        }
    }
}
