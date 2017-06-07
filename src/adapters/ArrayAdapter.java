package net.rentalhost.idea.adapters;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.ArrayCreationExpression;
import com.jetbrains.php.lang.psi.elements.ArrayHashElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ArrayAdapter implements Iterable<ArrayAdapter.ArrayElement> {
    private final List<PsiElement> arrayElements;

    @Nullable
    public static ArrayAdapter from(@Nullable final PhpTypedElement element) {
        if (!(element instanceof ArrayCreationExpression)) {
            return null;
        }

        final PhpPsiElement[] arrayElements = PsiTreeUtil.getChildrenOfType((PsiElement) element, PhpPsiElement.class);

        if (arrayElements == null) {
            return null;
        }

        return new ArrayAdapter(new ArrayList<>(Arrays.asList(arrayElements)));
    }

    private ArrayAdapter(final List<PsiElement> arrayElements) {
        this.arrayElements = arrayElements;
    }

    @NotNull
    @Override
    public Iterator<ArrayElement> iterator() {
        final Iterator<PsiElement> elementIterator = arrayElements.iterator();
        final AtomicInteger        elementIndex    = new AtomicInteger(0);

        return new Iterator<ArrayElement>() {
            @Override
            public boolean hasNext() {
                return elementIterator.hasNext();
            }

            @Override
            public ArrayElement next() {
                return new ArrayElement(elementIterator.next(), elementIndex);
            }
        };
    }

    public static class ArrayElement {
        private final PsiElement key;
        private final PsiElement value;
        private       boolean    isIndexed;

        ArrayElement(
            final PsiElement arrayElement,
            final AtomicInteger elementIndex
        ) {
            if (arrayElement instanceof ArrayHashElement) {
                key = ((ArrayHashElement) arrayElement).getKey();
                value = ((ArrayHashElement) arrayElement).getValue();
            }
            else {
                key = PhpPsiElementFactory.createFromText(arrayElement.getProject(), LeafPsiElement.class, String.valueOf(elementIndex.get()));
                value = ((PhpPsiElement) arrayElement).getFirstPsiChild();
                isIndexed = true;
                elementIndex.incrementAndGet();
            }
        }

        @NotNull
        public PsiElement getKey() {
            return key;
        }

        @NotNull
        public PsiElement getValue() {
            return value;
        }

        public boolean isIndexed() {
            return isIndexed;
        }
    }
}
