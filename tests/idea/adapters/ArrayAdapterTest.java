package net.rentalhost.idea.adapters;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.AssignmentExpression;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import org.junit.Assert;

import java.util.Iterator;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.suite.FixtureSuite;

public class ArrayAdapterTest extends FixtureSuite {
    @NotNull
    private static ArrayAdapter getArrayIterator(
        final PsiElement fileSample,
        final String arrayName
    ) {
        final AssignmentExpression arrayValue = (AssignmentExpression) getElementByName(fileSample, arrayName).getParent();
        return valueOf(ArrayAdapter.from((PhpTypedElement) arrayValue.getValue()));
    }

    public void testFrom() {
        final PsiFile fileSample = getResourceFile("adapters/ArrayAdapter.samples.php");

        Assert.assertNull(ArrayAdapter.from((PhpTypedElement) ((AssignmentExpression) getElementByName(fileSample, "ccNotAnArray").getParent()).getValue()));
        Assert.assertNull(ArrayAdapter.from((PhpTypedElement) ((AssignmentExpression) getElementByName(fileSample, "ccEmptyArray").getParent()).getValue()));

        final ArrayAdapter ccArrayHasNexts = getArrayIterator(fileSample, "indexed");

        for (final ArrayAdapter.ArrayElement ccArrayHasNext : ccArrayHasNexts) {
            Assert.assertNotNull(ccArrayHasNext.getKey());
        }

        final Iterator<ArrayAdapter.ArrayElement> arrayIndexed = getArrayIterator(fileSample, "indexed").iterator();

        final ArrayAdapter.ArrayElement arrayIndexed0 = arrayIndexed.next();
        Assert.assertEquals("0", valueOf(arrayIndexed0.getKey()).getText());
        Assert.assertEquals("1", valueOf(arrayIndexed0.getValue()).getText());
        Assert.assertTrue(arrayIndexed0.isIndexed());

        final ArrayAdapter.ArrayElement arrayIndexed1 = arrayIndexed.next();
        Assert.assertEquals("1", valueOf(arrayIndexed1.getKey()).getText());
        Assert.assertEquals("2", valueOf(arrayIndexed1.getValue()).getText());
        Assert.assertTrue(arrayIndexed1.isIndexed());

        final ArrayAdapter.ArrayElement arrayIndexed2 = arrayIndexed.next();
        Assert.assertEquals("2", valueOf(arrayIndexed2.getKey()).getText());
        Assert.assertEquals("3", valueOf(arrayIndexed2.getValue()).getText());
        Assert.assertTrue(arrayIndexed2.isIndexed());

        final Iterator<ArrayAdapter.ArrayElement> arrayKeyed = getArrayIterator(fileSample, "keyed").iterator();

        final ArrayAdapter.ArrayElement arrayKeyed0 = arrayKeyed.next();
        Assert.assertEquals("'a'", valueOf(arrayKeyed0.getKey()).getText());
        Assert.assertEquals("1", valueOf(arrayKeyed0.getValue()).getText());
        Assert.assertFalse(arrayKeyed0.isIndexed());

        final ArrayAdapter.ArrayElement arrayKeyed1 = arrayKeyed.next();
        Assert.assertEquals("'b'", valueOf(arrayKeyed1.getKey()).getText());
        Assert.assertEquals("2", valueOf(arrayKeyed1.getValue()).getText());
        Assert.assertFalse(arrayKeyed1.isIndexed());

        final ArrayAdapter.ArrayElement arrayKeyed2 = arrayKeyed.next();
        Assert.assertEquals("'c'", valueOf(arrayKeyed2.getKey()).getText());
        Assert.assertEquals("3", valueOf(arrayKeyed2.getValue()).getText());
        Assert.assertFalse(arrayKeyed2.isIndexed());

        final Iterator<ArrayAdapter.ArrayElement> arrayMixed = getArrayIterator(fileSample, "mixed").iterator();

        final ArrayAdapter.ArrayElement arrayMixed0 = arrayMixed.next();
        Assert.assertEquals("'a'", valueOf(arrayMixed0.getKey()).getText());
        Assert.assertEquals("1", valueOf(arrayMixed0.getValue()).getText());
        Assert.assertFalse(arrayMixed0.isIndexed());

        final ArrayAdapter.ArrayElement arrayMixed1 = arrayMixed.next();
        Assert.assertEquals("0", valueOf(arrayMixed1.getKey()).getText());
        Assert.assertEquals("2", valueOf(arrayMixed1.getValue()).getText());
        Assert.assertTrue(arrayMixed1.isIndexed());

        final ArrayAdapter.ArrayElement arrayMixed2 = arrayMixed.next();
        Assert.assertEquals("1", valueOf(arrayMixed2.getKey()).getText());
        Assert.assertEquals("3", valueOf(arrayMixed2.getValue()).getText());
        Assert.assertTrue(arrayMixed2.isIndexed());
    }
}
