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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocProperty;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.FieldReference;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.ParenthesizedExpression;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpReference;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.rentalhost.idea.adapters.ArrayAdapter;
import net.rentalhost.idea.laravelInsight.resources.CarbonClasses;
import net.rentalhost.idea.laravelInsight.resources.LaravelClasses;
import net.rentalhost.idea.utils.PhpClassUtil;
import net.rentalhost.idea.utils.PhpDocCommentUtil;
import net.rentalhost.idea.utils.PhpExpressionUtil;
import net.rentalhost.idea.utils.PhpFunctionUtil;
import net.rentalhost.idea.utils.PsiElementUtil;

public class ColumnWithoutAnnotationInspection extends PhpInspection {
    @NotNull private static final String messagePropertyUndefined = "@property $%s was not annotated";

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
            public void visitPhpClass(@NotNull final PhpClass phpClass) {
                if (PhpClassUtil.findSuperOfType(phpClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                    return;
                }

                InspectionHelper.reportTimestamps(problemsHolder, phpClass);
                InspectionHelper.reportPrimaryKey(problemsHolder, phpClass);
            }

            @Override
            public void visitPhpMethod(@NotNull final Method method) {
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
            public void visitPhpFieldReference(@NotNull final FieldReference fieldReference) {
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

                    InspectionHelper.validatePropertyAnnotation(problemsHolder, fieldClass, fieldNameNode.getPsi(), fieldNameText, null);
                    break;
                }
            }

            @Override
            public void visitPhpField(@NotNull final Field field) {
                final String  fieldName      = field.getName();
                final boolean isCastProperty = Objects.equals(fieldName, "casts");

                if (!isCastProperty &&
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

                final ArrayAdapter fieldElements = ArrayAdapter.from((PhpTypedElement) field.getDefaultValue());

                if (fieldElements == null) {
                    return;
                }

                for (final ArrayAdapter.ArrayElement element : fieldElements) {
                    PsiElement elementIssued = element.getValue();

                    final String elementName;
                    final String elementType;

                    if (!isCastProperty && element.isIndexed()) {
                        final PsiElement elementChildResolved = PhpExpressionUtil.resolve(elementIssued);

                        if (!(elementChildResolved instanceof StringLiteralExpression)) {
                            continue;
                        }

                        elementName = ((StringLiteralExpression) elementChildResolved).getContents();
                        elementType = CarbonClasses.CARBON.toString();
                    }
                    else if (isCastProperty && !element.isIndexed()) {
                        final PsiElement elementHashKey      = element.getKey();
                        final PsiElement elementHashKeyValue = PhpExpressionUtil.resolve(elementHashKey);

                        if (!(elementHashKeyValue instanceof StringLiteralExpression)) {
                            continue;
                        }

                        elementName = ((StringLiteralExpression) elementHashKeyValue).getContents();
                        elementIssued = elementHashKey;

                        final PsiElement fieldHashValue         = element.getValue();
                        final PsiElement fieldHashResolvedValue = PhpExpressionUtil.resolve(fieldHashValue);

                        if (!(fieldHashResolvedValue instanceof StringLiteralExpression)) {
                            continue;
                        }

                        elementType = InspectionHelper.getCastType(((StringLiteralExpression) fieldHashResolvedValue).getContents().toLowerCase());
                    }
                    else {
                        continue;
                    }

                    InspectionHelper.validatePropertyAnnotation(problemsHolder, fieldClass, elementIssued, elementName, elementType);
                }
            }

            @Override
            public void visitPhpUse(@NotNull final PhpUse expression) {
                if (expression.isTraitImport()) {
                    final PhpReference traitReferenceClass = expression.getTargetReference();
                    assert traitReferenceClass != null;

                    final PhpClass traitContainingClass = PhpClassUtil.getTraitContainingClass(expression);
                    assert traitContainingClass != null;

                    if (PhpClassUtil.findSuperOfType(traitContainingClass, LaravelClasses.ELOQUENT_MODEL.toString()) == null) {
                        return;
                    }

                    if (Objects.equals(traitReferenceClass.getFQN(), LaravelClasses.ELOQUENT_SOFTDELETES_TRAIT.toString())) {
                        InspectionHelper.validatePropertyAnnotation(problemsHolder, traitContainingClass, expression, "deleted_at", null);
                        return;
                    }

                    final PhpClass traitResolvedClass = (PhpClass) traitReferenceClass.resolve();

                    if (traitResolvedClass == null) {
                        return;
                    }

                    if (PhpClassUtil.findTraitOfType(traitResolvedClass, LaravelClasses.ELOQUENT_SOFTDELETES_TRAIT.toString()) == null) {
                        return;
                    }

                    InspectionHelper.validatePropertyAnnotation(problemsHolder, traitContainingClass, expression, "deleted_at", null);
                }
            }
        };
    }

    private enum InspectionHelper {
        ;

        @NotNull static final Map<String, String> CAST_TYPES = new HashMap<>();

        static {
            CAST_TYPES.put("int", "int");
            CAST_TYPES.put("integer", "int");
            CAST_TYPES.put("real", "float");
            CAST_TYPES.put("float", "float");
            CAST_TYPES.put("double", "float");
            CAST_TYPES.put("string", "string");
            CAST_TYPES.put("bool", "bool");
            CAST_TYPES.put("boolean", "bool");
            CAST_TYPES.put("object", "object");
            CAST_TYPES.put("array", "array");
            CAST_TYPES.put("json", "array");
            CAST_TYPES.put("collection", LaravelClasses.ELOQUENT_COLLECTION.toString());
            CAST_TYPES.put("date", CarbonClasses.CARBON.toString());
            CAST_TYPES.put("datetime", CarbonClasses.CARBON.toString());
            CAST_TYPES.put("timestamp", CarbonClasses.CARBON.toString());
        }

        static void reportTimestamps(
            @NotNull final ProblemsHolder problemsHolder,
            @NotNull final PhpClass phpClass
        ) {
            final Field fieldTimestamps = PhpClassUtil.findPropertyDeclaration(phpClass, "timestamps");

            if (fieldTimestamps == null) {
                return;
            }

            final PsiElement fieldTimestampsDefaultValue = fieldTimestamps.getDefaultValue();

            if (!(fieldTimestampsDefaultValue instanceof PhpExpression)) {
                return;
            }

            final PsiElement fieldTimestampsDefaultValueResolved = PhpExpressionUtil.resolve(fieldTimestampsDefaultValue);

            if (!(fieldTimestampsDefaultValueResolved instanceof ConstantReference)) {
                return;
            }

            if (!"true".equals(fieldTimestampsDefaultValueResolved.getText())) {
                return;
            }

            final PsiElement issueReceptor = getReportableElement(phpClass, fieldTimestamps);

            if (issueReceptor == null) {
                return;
            }

            validatePropertyAnnotation(problemsHolder, phpClass, issueReceptor, "created_at", null);
            validatePropertyAnnotation(problemsHolder, phpClass, issueReceptor, "updated_at", null);
        }

        static void validatePropertyAnnotation(
            @NotNull final ProblemsHolder problemsHolder,
            @NotNull final PhpClass phpClass,
            @NotNull final PsiElement issueReference,
            @NotNull final String propertyName,
            @Nullable final String propertyType
        ) {
            if (propertyName.isEmpty()) {
                return;
            }

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
                String propertyTypeIdentified = propertyType;

                if (propertyTypeIdentified == null) {
                    if (propertyName.endsWith("_id")) {
                        propertyTypeIdentified = "int";
                    }
                    else if (propertyName.endsWith("_at")) {
                        propertyTypeIdentified = CarbonClasses.CARBON.toString();
                    }
                    else {
                        propertyTypeIdentified = "mixed";
                    }
                }

                registerPropertyUndefined(problemsHolder, phpClass, issueReference, propertyName, propertyTypeIdentified);
            }
        }

        static void registerPropertyUndefined(
            @NotNull final ProblemsHolder problemsHolder,
            @NotNull final PhpClass primaryClass,
            @NotNull final PsiElement issuedElement,
            @NotNull final String propertyName,
            @NotNull final String propertyType
        ) {
            problemsHolder.registerProblem(issuedElement,
                                           String.format(messagePropertyUndefined, propertyName),
                                           ProblemHighlightType.WEAK_WARNING,
                                           new InspectionQuickFix(primaryClass, propertyName, propertyType));
        }

        static void reportPrimaryKey(
            @NotNull final ProblemsHolder problemsHolder,
            @NotNull final PhpClass phpClass
        ) {
            final PsiElement issueReceptor;
            final Field      fieldPrimaryKey              = PhpClassUtil.findPropertyDeclaration(phpClass, "primaryKey");
            String           fieldPrimaryKeyResolvedValue = "id";

            if (fieldPrimaryKey != null) {
                final PsiElement fieldPrimaryKeyValue = fieldPrimaryKey.getDefaultValue();

                if (!(fieldPrimaryKeyValue instanceof PhpExpression)) {
                    return;
                }

                final PsiElement fieldPrimaryKeyResolved = PhpExpressionUtil.resolve(fieldPrimaryKeyValue);

                if (!(fieldPrimaryKeyResolved instanceof StringLiteralExpression)) {
                    return;
                }

                fieldPrimaryKeyResolvedValue = ((StringLiteralExpression) fieldPrimaryKeyResolved).getContents();
                issueReceptor = getReportableElement(phpClass, fieldPrimaryKey);
            }
            else {
                final PsiElement issueReceptorPrimary = phpClass.getNameIdentifier();
                issueReceptor = (issueReceptorPrimary == null) ? phpClass : issueReceptorPrimary;
            }

            if (issueReceptor == null) {
                return;
            }

            final Field fieldKeyType = PhpClassUtil.findPropertyDeclaration(phpClass, "keyType");
            String      fieldType    = "int";

            if (fieldKeyType != null) {
                final PsiElement fieldKeyTypeValueRaw = fieldKeyType.getDefaultValue();

                if (fieldKeyTypeValueRaw != null) {
                    final PsiElement fieldKeyTypeValue = PhpExpressionUtil.resolve(fieldKeyTypeValueRaw);

                    if (fieldKeyTypeValue instanceof StringLiteralExpression) {
                        fieldType = ((StringLiteralExpression) fieldKeyTypeValue).getContents();
                    }
                }
            }

            validatePropertyAnnotation(problemsHolder, phpClass, issueReceptor, fieldPrimaryKeyResolvedValue, fieldType);
        }

        static void reportAccessorOrMutator(
            @NotNull final ProblemsHolder problemsHolder,
            @NotNull final PsiNameIdentifierOwner method,
            @NotNull final PhpClass methodClass,
            @NotNull final String methodName
        ) {
            if (methodName.endsWith("Attribute")) {
                final boolean isAccessor = methodName.startsWith("get");
                final boolean isMutator  = !isAccessor && methodName.startsWith("set");

                if (isAccessor || isMutator) {
                    final PsiElement methodIdentifier = method.getNameIdentifier();
                    assert methodIdentifier != null;

                    Function methodReference = (Function) method;

                    if (isMutator) {
                        final Method methodAccessor = PhpClassUtil.findMethodDeclaration(methodClass, "get" + methodName.substring(3));

                        if (methodAccessor != null) {
                            methodReference = methodAccessor;
                        }
                    }

                    String methodReturnType = "mixed";

                    if (isAccessor || !Objects.equals(methodReference, method)) {
                        methodReturnType = PhpFunctionUtil.getReturnType(methodReference).toString();
                    }

                    final String methodPropertyPart = methodName.substring(3, methodName.length() - 9);

                    validatePropertyAnnotation(problemsHolder, methodClass, methodIdentifier,
                                               CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, methodPropertyPart), methodReturnType);
                }
            }
        }

        static void reportRelationship(
            @NotNull final ProblemsHolder problemsHolder,
            @NotNull final Function method,
            @NotNull final PhpClass methodClass
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
        static String getCastType(final String castKey) {
            if (CAST_TYPES.containsKey(castKey)) {
                return CAST_TYPES.get(castKey);
            }

            return "mixed";
        }

        @Nullable
        private static PsiElement getReportableElement(
            @NotNull final PsiNameIdentifierOwner phpClass,
            @NotNull final PhpClassMember fieldPrimaryKey
        ) {
            if (Objects.equals(fieldPrimaryKey.getContainingClass(), phpClass)) {
                return fieldPrimaryKey.getNameIdentifier();
            }

            return phpClass.getNameIdentifier();
        }

        private static boolean isRelationship(@NotNull final Collection<String> functionTypes) {
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

    static class InspectionQuickFix implements LocalQuickFix {
        @NotNull private final SmartPsiElementPointer<PhpClass> primaryClassPointer;
        @NotNull private final String                           propertyName;
        @NotNull private final String                           propertyType;
        @NotNull private final String                           familyName;

        InspectionQuickFix(
            @NotNull final PhpClass primaryClass,
            @NotNull final String propertyName,
            @NotNull final String propertyType
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
