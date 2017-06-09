package net.rentalhost.idea.laravelInsight.scope;

import com.google.common.base.CaseFormat;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIcons;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.PhpPresentationUtil;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.FieldReference;
import com.jetbrains.php.lang.psi.elements.MemberReference;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpExpression;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.idea.laravelInsight.resources.LaravelClasses;

public class ScopeCompletionContributor extends CompletionContributor {
    public ScopeCompletionContributor() {
        extend(CompletionType.BASIC, CompletionContributorHelper.arrowedReference(), new CompletionContributorProvider());
    }

    enum CompletionContributorHelper {
        ;

        static ElementPattern<PsiElement> arrowedReference() {
            final PsiElementPattern.Capture<PsiElement> arrowedCase = PlatformPatterns.psiElement(PhpTokenTypes.IDENTIFIER).afterLeaf("->");

            return StandardPatterns.or(
                arrowedCase.withParent(FieldReference.class),
                arrowedCase.withParent(MethodReference.class)
            );
        }
    }

    static class CompletionContributorProvider extends CompletionProvider<CompletionParameters> {
        @Override
        public void addCompletions(
            @NotNull final CompletionParameters parameters,
            final ProcessingContext context,
            @NotNull final CompletionResultSet result
        ) {
            final PsiElement      element                       = parameters.getPosition();
            final MemberReference elementReference              = (MemberReference) element.getParent();
            final PhpExpression   elementClassReferenceRelative = elementReference.getClassReference();
            assert elementClassReferenceRelative != null;

            final Set<String>          elementClassReferenceAbsolute = elementClassReferenceRelative.getType().globalLocationAware(elementClassReferenceRelative).getTypes();
            final Collection<PhpClass> elementClasses                = PhpIndex.getInstance(element.getProject()).getAnyByFQN(elementClassReferenceAbsolute.iterator().next());
            final PhpClass             elementClass                  = elementClasses.iterator().next();

            if (elementClass == null) {
                return;
            }

            for (final Method method : elementClass.getMethods()) {
                if (method.getName().startsWith("scope")) {
                    final String methodSliced = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, method.getName().substring(5));

                    result.addElement(new CompletionContributorLookupElement(element, method, methodSliced));
                }
            }
        }
    }

    static class CompletionContributorLookupElement extends LookupElement {
        private final PsiElement element;
        private final Method     method;
        private final String     lookupString;

        CompletionContributorLookupElement(
            final PsiElement element,
            final Method method,
            final String lookupString
        ) {
            this.element = element;
            this.method = method;
            this.lookupString = lookupString;
        }

        @NotNull
        @Override
        public String getLookupString() {
            return lookupString;
        }

        @Override
        public void handleInsert(final InsertionContext context) {
            if (element.getParent() instanceof FieldReference) {
                final Editor     editor     = context.getEditor();
                final CaretModel caretModel = editor.getCaretModel();
                final Document   document   = editor.getDocument();

                document.insertString(caretModel.getOffset(), "()");
                caretModel.moveCaretRelatively(1, 0, false, false, false);
            }
        }

        @Override
        public void renderElement(final LookupElementPresentation presentation) {
            final Parameter[] methodParameters = method.getParameters();
            final Parameter[] methodParametersWithoutBuilder = (methodParameters.length >= 1)
                                                               ? Arrays.copyOfRange(methodParameters, 1, methodParameters.length)
                                                               : methodParameters;

            presentation.setItemText(lookupString);
            presentation.setIcon(PhpIcons.METHOD);
            presentation.setTypeText(LaravelClasses.ELOQUENT_BUILDER.toString().substring(1));
            presentation.setTypeGrayed(false);
            presentation.appendTailText(PhpPresentationUtil.formatParameters(null, methodParametersWithoutBuilder).toString(), true);
        }

        @Override
        public boolean isCaseSensitive() {
            return false;
        }
    }
}
