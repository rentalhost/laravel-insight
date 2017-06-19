package net.rentalhost.idea.laravelInsight.utils;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocProperty;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.PhpClass;

import java.util.Objects;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import net.rentalhost.idea.utils.PhpDocCommentUtil;

public class PropertyQuickFix implements LocalQuickFix {
    @NotNull private final SmartPsiElementPointer<PhpClass> primaryClassPointer;
    @NotNull private final String                           propertyName;
    @NotNull private final String                           propertyType;
    @NotNull private final String                           familyName;

    public PropertyQuickFix(
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
