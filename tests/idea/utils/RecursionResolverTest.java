package net.rentalhost.idea.utils;

import org.junit.Assert;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.rentalhost.suite.FixtureSuite;

public class RecursionResolverTest extends FixtureSuite {
    @NotNull
    private static Function<RecursionResolver.Resolver, Integer> getResolverIntegerFunction() {
        return resolver -> {
            final TestObject testObject = (TestObject) resolver.getObject();
            final TestObject testChild  = testObject.getObjectChild();
            Integer          testSum    = testObject.getObjectValue();

            if (testChild != null) {
                final Integer testChildResolved = (Integer) resolver.resolve(testChild);

                if (testChildResolved != null) {
                    testSum += testChildResolved;
                }
            }

            return testSum;
        };
    }

    public void testResolve() {
        final TestObject testObjectA_endRecursion = new TestObject(1, null);
        final TestObject testObjectB_endRecursion = new TestObject(2, testObjectA_endRecursion);
        final TestObject testObjectC_endRecursion = new TestObject(3, testObjectB_endRecursion);

        Assert.assertEquals(Integer.valueOf(6), RecursionResolver.resolve(testObjectC_endRecursion, getResolverIntegerFunction()));
        Assert.assertEquals(Integer.valueOf(3), RecursionResolver.resolve(testObjectB_endRecursion, getResolverIntegerFunction()));
        Assert.assertEquals(Integer.valueOf(1), RecursionResolver.resolve(testObjectA_endRecursion, getResolverIntegerFunction()));

        final TestObject testObjectA_endlessRecursion = new TestObject(1, null);
        final TestObject testObjectB_endlessRecursion = new TestObject(2, testObjectA_endlessRecursion);
        testObjectA_endlessRecursion.setObjectChild(testObjectB_endlessRecursion);

        // Both should return 3 because:
        // If start from "A": (1) from A + (2) from B, then stop recursion; or,
        // If start from "B": (2) from B + (1) from A, then stop recursion.
        Assert.assertEquals(Integer.valueOf(3), RecursionResolver.resolve(testObjectA_endlessRecursion, getResolverIntegerFunction()));
        Assert.assertEquals(Integer.valueOf(3), RecursionResolver.resolve(testObjectB_endlessRecursion, getResolverIntegerFunction()));
    }

    static class TestObject {
        private final     int        objectValue;
        @Nullable private TestObject objectChild;

        TestObject(
            final int objectValue,
            @Nullable final TestObject objectChild
        ) {
            this.objectValue = objectValue;
            this.objectChild = objectChild;
        }

        @Nullable
        TestObject getObjectChild() {
            return objectChild;
        }

        void setObjectChild(@NotNull final TestObject objectChild) {
            this.objectChild = objectChild;
        }

        int getObjectValue() {
            return objectValue;
        }
    }
}
