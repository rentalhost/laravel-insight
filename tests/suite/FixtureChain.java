package net.rentalhost.suite;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

import java.lang.reflect.InvocationTargetException;

import org.jetbrains.annotations.NotNull;

public class FixtureChain {
    private final CodeInsightTestFixture fixture;

    FixtureChain(final CodeInsightTestFixture myFixture) {
        fixture = myFixture;
    }

    @NotNull
    FixtureChain addInspectionTool(@NotNull final Class<? extends LocalInspectionTool> inspectionTool) {
        try {
            fixture.enableInspections(inspectionTool.getConstructor().newInstance());
        }
        catch (@NotNull InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
        }

        return this;
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
}
