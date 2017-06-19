package net.rentalhost.idea.utils;

import com.google.common.collect.Iterables;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocProperty;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocPropertyTag;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PhpDocCommentUtil {
    ;

    @Nullable
    public static PhpDocProperty findProperty(
        @NotNull final PhpDocComment docComment,
        @NotNull final String propertyName
    ) {
        final List<PhpDocPropertyTag> classPropertyTags = docComment.getPropertyTags();

        for (final PhpDocPropertyTag classPropertyTag : classPropertyTags) {
            final PhpDocProperty property = classPropertyTag.getProperty();

            if ((property != null) && Objects.equals(property.getName(), propertyName)) {
                return property;
            }
        }

        return null;
    }

    @Nullable
    public static PhpDocProperty findPropertyRecursively(
        @NotNull final PhpClass phpClass,
        @NotNull final String propertyName
    ) {
        PhpClass phpClassCurrent = phpClass;

        while (phpClassCurrent != null) {
            final PhpDocComment classDocComment = phpClassCurrent.getDocComment();

            if (classDocComment != null) {
                final PhpDocProperty propertyFound = findProperty(classDocComment, propertyName);

                if (propertyFound != null) {
                    return propertyFound;
                }
            }

            phpClassCurrent = PhpClassUtil.getSuper(phpClassCurrent);
        }

        return null;
    }

    @NotNull
    public static PhpDocTag createTag(
        @NotNull final PhpNamedElement element,
        @NotNull final String tagName
    ) {
        return createTag(element, tagName, null);
    }

    public static PhpDocTag createTag(
        @NotNull final PhpNamedElement element,
        @NotNull final String tagName,
        @Nullable final String tagContents
    ) {
        final PhpDocComment docComment            = element.getDocComment();
        final StringBuilder docCommentTailBuilder = new StringBuilder(4).append(tagName);

        if (tagContents != null) {
            docCommentTailBuilder.append(' ').append(tagContents);
        }

        docCommentTailBuilder.append("\n*/ $r");

        final String docCommentTail = docCommentTailBuilder.toString();

        if (docComment == null) {
            final String        docCommentContents = "/**\n* " + docCommentTail;
            final PhpDocComment docCommentElement  = PhpPsiElementFactory.createFromText(element.getProject(), PhpDocComment.class, docCommentContents);
            assert docCommentElement != null;

            element.getParent().addBefore(docCommentElement, element);

            return docCommentElement.getTagElementsByName(tagName)[0];
        }

        final List<PhpDocTag> docCommentLastTags = Arrays.asList(docComment.getTagElementsByName(tagName));

        if (!docCommentLastTags.isEmpty()) {
            final PhpDocTag docCommentLastTag = Iterables.getLast(docCommentLastTags);

            return appendPhpDogTag(element, docComment, docCommentTail, docCommentLastTag);
        }

        final Collection<PhpDocTag> docCommentLastAnyTags = PsiTreeUtil.findChildrenOfType(docComment, PhpDocTag.class);

        if (docCommentLastAnyTags.isEmpty()) {
            final PsiElement docCommentLastChild = docComment.getLastChild().getPrevSibling();

            return appendPhpDogTag(element, docComment, docCommentTail, docCommentLastChild);
        }

        final PhpDocTag docCommentLastTag = Iterables.getLast(docCommentLastAnyTags);

        return appendPhpDogTag(element, docComment, docCommentTail, docCommentLastTag);
    }

    @NotNull
    private static PhpDocTag appendPhpDogTag(
        @NotNull final PsiElement element,
        @NotNull final PhpDocComment docComment,
        @NotNull final String docCommentTail,
        @NotNull final PsiElement docCommentLastTag
    ) {
        final String    docCommentContents = "/**\n*" + docCommentTail;
        final PhpDocTag docCommentNewTag   = PhpPsiElementFactory.createFromText(element.getProject(), PhpDocTag.class, docCommentContents);
        assert docCommentNewTag != null;

        final PsiElement docCommentAsterisk = docCommentNewTag.getPrevSibling();
        final PsiElement docAsteriskNew     = docComment.addAfter(docCommentAsterisk, docCommentLastTag);

        return (PhpDocTag) docComment.addAfter(docCommentNewTag, docAsteriskNew);
    }
}
