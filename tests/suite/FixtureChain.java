package net.rentalhost.suite;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

public class FixtureChain {
    private final CodeInsightTestFixture fixture;
    private       LocalInspectionTool    inspectionTool;

    FixtureChain(final CodeInsightTestFixture myFixture) {
        fixture = myFixture;
    }

    @NotNull
    public FixtureChain addTestFile(@NotNull final String testFile) {
        fixture.configureByFile("resources-tests/" + testFile);

        return this;
    }

    @NotNull
    public FixtureChain highlightTest() {
        fixture.testHighlighting(true, false, true);

        return this;
    }

    public void quickFixesTest(final String testFile) {
        for (final IntentionAction quickFix : fixture.getAllQuickFixes()) {
            fixture.launchAction(quickFix);
        }

        fixture.setTestDataPath(".");

        final String testFilePath  = "resources-tests/" + testFile;
        final String fixedFilePath = testFilePath.substring(0, testFilePath.length() - 4) + ".fixed.php";

        fixture.checkResultByFile(fixedFilePath);
    }

    public void runVisitor(final Consumer<PhpElementVisitor> consumer) {
        final ProblemsHolder    problemsHolder    = new ProblemsHolder(InspectionManager.getInstance(fixture.getProject()), fixture.getFile(), true);
        final PhpElementVisitor psiElementVisitor = (PhpElementVisitor) inspectionTool.buildVisitor(problemsHolder, false);

        consumer.accept(psiElementVisitor);
    }

    @NotNull
    FixtureChain addInspectionTool(@NotNull final Class<? extends LocalInspectionTool> localInspectionTool) {
        try {
            inspectionTool = localInspectionTool.getConstructor().newInstance();
            fixture.enableInspections(inspectionTool);
        }
        catch (@NotNull InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
        }

        return this;
    }
}
