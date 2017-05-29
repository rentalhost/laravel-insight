package net.rentalhost.suite;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

import java.lang.reflect.InvocationTargetException;

public class FixtureChain {
    private final CodeInsightTestFixture fixture;

    FixtureChain(final CodeInsightTestFixture myFixture) {
        fixture = myFixture;
    }

    FixtureChain addInspectionTool(final Class<? extends LocalInspectionTool> inspectionTool) {
        try {
            fixture.enableInspections(inspectionTool.getConstructor().newInstance());
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
        }

        return this;
    }

    public FixtureChain addTestFile(final String testFile) {
        fixture.configureByFile("resources-tests/" + testFile);

        return this;
    }

    public void highlightTest() {
        fixture.testHighlighting(true, false, true);
    }
}
