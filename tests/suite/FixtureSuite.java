package net.rentalhost.suite;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FixtureSuite extends CodeInsightFixtureTestCase {
    @NotNull
    protected static <T> T valueOf(@Nullable final T element) {
        assert element != null;

        return element;
    }

    @NotNull
    protected static PhpNamedElement getElementByName(
        @Nullable final PsiElement file,
        final String elementName
    ) {
        assert file != null;

        final Iterable<PhpNamedElement> nameElements = new ArrayList<>(PsiTreeUtil.findChildrenOfType(file, PhpNamedElement.class));

        for (final PhpNamedElement namedElement : nameElements) {
            if (namedElement.getName().equals(elementName)) {
                return namedElement;
            }
        }

        return valueOf(null);
    }

    protected FixtureChain inspectTool(final Class<? extends LocalInspectionTool> inspectionTool) {
        return new FixtureChain(myFixture).addInspectionTool(inspectionTool);
    }

    @NotNull
    protected PsiFile getResourceFile(final String path) {
        return valueOf(myFixture.configureByFile("resources-tests/" + path));
    }
}
