package net.rentalhost.idea.api;

import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.suite.FixtureSuite;

public class PhpClassUtilTest extends FixtureSuite {
    public void testFindSuperOfType() {
        final List<PhpClass> fileClasses = getPhpClasses();

        // Bogus assertions...
        Assert.assertNull(PhpClassUtil.findSuperOfType(fileClasses.get(0), "\\ThisClassDontHaveParent"));
        Assert.assertNull(PhpClassUtil.findSuperOfType(fileClasses.get(1), "\\ThisDoesButNotThat"));
        Assert.assertNull(PhpClassUtil.findSuperOfType(fileClasses.get(2), "\\ThisDoesButNotThat"));

        // FirstClass is parent of SecondClass.
        Assert.assertNotNull(PhpClassUtil.findSuperOfType(fileClasses.get(1), "\\FirstClass"));

        // FirstClass and SecondClass are parent of ThirdClass.
        Assert.assertNotNull(PhpClassUtil.findSuperOfType(fileClasses.get(2), "\\SecondClass"));
        Assert.assertNotNull(PhpClassUtil.findSuperOfType(fileClasses.get(2), "\\FirstClass"));

        // CC_UnresolvableParentClass is parent of CC_ChildClass (case #1).
        // CC_UnresolvableParentClass is unresolvable, then we can't try search after that (case #2).
        Assert.assertNotNull(PhpClassUtil.findSuperOfType(fileClasses.get(3), "\\CC_UnresolvableParentClass"));
        Assert.assertNull(PhpClassUtil.findSuperOfType(fileClasses.get(3), "\\CC_ThereIsNothingAfterUnresolvableParentClass"));
    }

    public void testHasSuperOfType() {
        final List<PhpClass> fileClasses = getPhpClasses();

        // Bogus assertions...
        Assert.assertFalse(PhpClassUtil.hasSuperOfType(fileClasses.get(0), "\\ThisClassDontHaveParent"));
        Assert.assertFalse(PhpClassUtil.hasSuperOfType(fileClasses.get(1), "\\ThisDoesButNotThat"));
        Assert.assertFalse(PhpClassUtil.hasSuperOfType(fileClasses.get(2), "\\ThisDoesButNotThat"));

        // FirstClass is parent of SecondClass.
        Assert.assertTrue(PhpClassUtil.hasSuperOfType(fileClasses.get(1), "\\FirstClass"));

        // FirstClass and SecondClass are parent of ThirdClass.
        Assert.assertTrue(PhpClassUtil.hasSuperOfType(fileClasses.get(2), "\\SecondClass"));
        Assert.assertTrue(PhpClassUtil.hasSuperOfType(fileClasses.get(2), "\\FirstClass"));

        // CC_UnresolvableParentClass is parent of CC_ChildClass (case #1).
        // CC_UnresolvableParentClass is unresolvable, then we can't try search after that (case #2).
        Assert.assertTrue(PhpClassUtil.hasSuperOfType(fileClasses.get(3), "\\CC_UnresolvableParentClass"));
        Assert.assertFalse(PhpClassUtil.hasSuperOfType(fileClasses.get(3), "\\CC_ThereIsNothingAfterUnresolvableParentClass"));
    }

    public void testGetSuperReference() {
        final List<PhpClass> fileClasses = getPhpClasses();

        // FirstClass have no super class.
        Assert.assertNull(PhpClassUtil.getSuperReference(fileClasses.get(0)));

        // FirstClass is the super class of SecondClass.
        // Checking just one level, because is not a recursive method.
        final ClassReference superReference = PhpClassUtil.getSuperReference(fileClasses.get(1));
        Assert.assertNotNull(superReference);
        Assert.assertEquals("\\FirstClass", superReference.getFQN());
    }

    public void testGetSuper() {
        final List<PhpClass> fileClasses = getPhpClasses();

        // FirstClass have no super class.
        Assert.assertNull(PhpClassUtil.getSuper(fileClasses.get(0)));

        // FirstClass is the super class of SecondClass.
        // Checking just one level, because is not a recursive method.
        Assert.assertEquals(fileClasses.get(0), PhpClassUtil.getSuper(fileClasses.get(1)));
    }

    @NotNull
    private List<PhpClass> getPhpClasses() {
        final PsiFile        fileSample  = getResourceFile("api/PhpClassUtil.superClasses.php");
        final List<PhpClass> fileClasses = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpClass.class));

        Assert.assertEquals(4, fileClasses.size());

        return fileClasses;
    }
}
