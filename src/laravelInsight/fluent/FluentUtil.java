package net.rentalhost.idea.laravelInsight.fluent;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.apache.commons.lang.StringUtils;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.rentalhost.idea.laravelInsight.resources.LaravelClasses;
import net.rentalhost.idea.utils.PhpClassUtil;

enum FluentUtil {
    ;

    static boolean isUsingDirectly(@Nullable final PsiElement parameter) {
        if (parameter == null) {
            return false;
        }

        final List<PhpClass> expressionClasses = PhpClassUtil.resolve(parameter);

        if (expressionClasses.isEmpty()) {
            return false;
        }

        final PhpClass expressionClass    = expressionClasses.get(0);
        final String   expressionClassFQN = expressionClass.getFQN();
        final boolean isDirectInstance = expressionClassFQN.equals(LaravelClasses.SUPPORT_FLUENT.toString()) ||
                                         expressionClassFQN.equals(LaravelClasses.SUPPORT_FLUENT_L54.toString());

        if (!isDirectInstance &&
            (PhpClassUtil.findSuperOfType(expressionClass, LaravelClasses.SUPPORT_FLUENT.toString()) == null)) {
            return false;
        }

        // Case #1: new \Illuminate\Support\Fluent (directly) or
        //          new \Facades\Illuminate\Support\Fluent (facade, from Laravel 5.4);
        // Case #2: new \Fluent (facade);
        return isDirectInstance ||
               (StringUtils.countMatches(expressionClassFQN, "\\") == 1);
    }
}
