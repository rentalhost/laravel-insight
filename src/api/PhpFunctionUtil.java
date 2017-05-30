package net.rentalhost.idea.api;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;

import org.jetbrains.annotations.Nullable;

public enum PhpFunctionUtil {
    ;

    @Nullable
    public static PhpType getReturnType(final PhpNamedElement function) {
        final PhpDocComment functionDocComment = function.getDocComment();

        if (functionDocComment != null) {
            final PhpDocReturnTag functionDocReturnTag = functionDocComment.getReturnTag();

            if (functionDocReturnTag != null) {
                return functionDocReturnTag.getType();
            }
        }

        return null;
    }
}
