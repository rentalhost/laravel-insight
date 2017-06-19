package net.rentalhost.suite;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.OffsetMap;
import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.codeInsight.lookup.LookupArranger;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.jetbrains.php.lang.psi.elements.AssignmentExpression;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FixtureSuite extends CodeInsightFixtureTestCase {
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

    @NotNull
    protected static PhpPsiElement getElementAssignmentValueByName(
        @Nullable final PsiElement file,
        final String elementName
    ) {
        final PhpNamedElement element       = getElementByName(file, elementName);
        final PsiElement      elementParent = element.getParent();

        if (elementParent instanceof AssignmentExpression) {
            return valueOf(((AssignmentExpression) elementParent).getValue());
        }

        return valueOf(null);
    }

    @NotNull
    protected static <T> T valueOf(@Nullable final T element) {
        assert element != null;

        return element;
    }

    @NotNull
    protected FixtureChain inspectTool(@NotNull final Class<? extends LocalInspectionTool> inspectionTool) {
        return new FixtureChain(myFixture).addInspectionTool(inspectionTool);
    }

    @NotNull
    protected PsiFile getResourceFile(final String path) {
        return valueOf(myFixture.configureByFile("resources-tests/" + path));
    }

    @Nullable
    protected <T> T runWriteAction(@NotNull final Supplier<T> supplier) {
        final AtomicReference<T> supplierResponse = new AtomicReference<>();

        new WriteCommandAction.Simple(myFixture.getProject()) {
            @Override
            protected void run() throws Throwable {
                supplierResponse.set(supplier.get());
            }
        }.execute();

        return supplierResponse.get();
    }

    protected void runWriteAction(@NotNull final Runnable consumer) {
        new WriteCommandAction.Simple(myFixture.getProject()) {
            @Override
            protected void run() throws Throwable {
                consumer.run();
            }
        }.execute();
    }

    @NotNull
    protected LookupElement[] getCompletionElements(
        final PsiElement reference,
        final int referenceDistance
    ) {
        myFixture.getEditor().getCaretModel().getPrimaryCaret().moveToOffset(reference.getTextOffset() + referenceDistance);
        myFixture.completeBasic();

        return valueOf(myFixture.getLookupElements());
    }

    protected void coverageHandleInsert(
        final PsiFile fileSample,
        final LookupElement completionElement
    ) {
        runWriteAction(() -> {
            final InsertionContext insertionContext = getInsertionContext(fileSample, completionElement);
            completionElement.handleInsert(insertionContext);
        });
    }

    protected void moveCaret(final int offset) {
        getEditor().getCaretModel().moveToOffset(offset);
    }

    protected void acceptLookupElement(final LookupElement completionElement) {
        final LookupImpl lookup = (LookupImpl) LookupManager
            .getInstance(getProject())
            .createLookup(getEditor(), new LookupElement[] { completionElement }, "", new LookupArranger.DefaultArranger());
        lookup.finishLookup('\n');
    }

    @SuppressWarnings ("TestMethodWithIncorrectSignature")
    protected void testFolding(final String foldingFile) {
        final String filePath = "resources-tests/" + foldingFile;

        myFixture.configureByFile(filePath);
        myFixture.testFolding(filePath);
    }

    @NotNull
    private InsertionContext getInsertionContext(
        final PsiFile fileSample,
        final LookupElement completionElement
    ) {
        final Editor fixtureEditor = myFixture.getEditor();

        return new InsertionContext(new OffsetMap(fixtureEditor.getDocument()),
                                    Lookup.NORMAL_SELECT_CHAR,
                                    new LookupElement[] { completionElement },
                                    fileSample,
                                    fixtureEditor,
                                    false);
    }
}
