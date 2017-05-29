package net.rentalhost.idea.api;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.*;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.suite.FixtureSuite;

public class PhpClassUtilTest extends FixtureSuite {
    public void testGetTraitsDeclared() {
        final PsiFile        fileSample  = getResourceFile("api/PhpClassUtil.traitsDeclaration.php");
        final List<PhpClass> fileClasses = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpClass.class));

        Assert.assertEquals(1, fileClasses.size());

        final ArrayList<PhpUse> phpUses = Lists.newArrayList(PhpClassUtil.getTraitsDeclared(fileClasses.get(0)));

        Assert.assertSame(3, phpUses.size());
        Assert.assertEquals("\\FirstTrait", phpUses.get(0).getFQN());
        Assert.assertEquals("\\SecondTrait", phpUses.get(1).getFQN());
        Assert.assertEquals("\\ThirdTrait", phpUses.get(2).getFQN());
    }

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

    public void testFindPropertyDeclaration() {
        final PsiFile        fileSample  = getResourceFile("api/PhpClassUtil.findDeclaration.php");
        final List<PhpClass> fileClasses = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpClass.class));

        Assert.assertEquals(5, fileClasses.size());

        // Bogus assertions...
        final PhpClass classFirstClass  = fileClasses.get(2);
        final PhpClass classSecondClass = fileClasses.get(3);
        final PhpClass classThirdClass  = fileClasses.get(4);

        Assert.assertNull(PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyInexistent"));
        Assert.assertNull(PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyInexistent"));
        Assert.assertNull(PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyInexistent"));

        // FirstClass have only $propertyFromFirst.
        final Field propertyFromFirst = PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyFromFirst");
        Assert.assertNotNull(propertyFromFirst);
        Assert.assertEquals(classFirstClass, propertyFromFirst.getContainingClass());

        // SecondClass have both $propertyFromFirst (from #1) and $propertyFromSecond.
        final Field propertyFromFirst1 = PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromFirst");
        Assert.assertNotNull(propertyFromFirst1);
        Assert.assertEquals(classFirstClass, propertyFromFirst1.getContainingClass());
        final Field propertyFromSecond = PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromSecond");
        Assert.assertNotNull(propertyFromSecond);
        Assert.assertEquals(classSecondClass, propertyFromSecond.getContainingClass());

        // SecondClass have all $propertyFromFirst (from #1) and $propertyFromSecond (from #2) and $propertyFromThird.
        final Field propertyFromFirst2 = PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromFirst");
        Assert.assertNotNull(propertyFromFirst2);
        Assert.assertEquals(classFirstClass, propertyFromFirst2.getContainingClass());
        final Field propertyFromSecond1 = PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromSecond");
        Assert.assertNotNull(propertyFromSecond1);
        Assert.assertEquals(classSecondClass, propertyFromSecond1.getContainingClass());
        final Field propertyFromThird = PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromThird");
        Assert.assertNotNull(propertyFromThird);
        Assert.assertEquals(classThirdClass, propertyFromThird.getContainingClass());

        final PhpClass traitFirstTrait  = fileClasses.get(0);
        final PhpClass traitSecondTrait = fileClasses.get(1);

        // $firstPropertyOnTrait should be detected from FirstClass, SecondClass or ThirdClass.
        final Field propertyFromFirstTrait = PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyFromFirstTrait");
        Assert.assertNotNull(propertyFromFirstTrait);
        Assert.assertEquals(traitFirstTrait, propertyFromFirstTrait.getContainingClass());
        final Field propertyFromFirstTrait1 = PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromFirstTrait");
        Assert.assertNotNull(propertyFromFirstTrait1);
        Assert.assertEquals(traitFirstTrait, propertyFromFirstTrait1.getContainingClass());
        final Field propertyFromFirstTrait2 = PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromFirstTrait");
        Assert.assertNotNull(propertyFromFirstTrait2);
        Assert.assertEquals(traitFirstTrait, propertyFromFirstTrait2.getContainingClass());

        // $secondPropertyOnTrait should be detected from SecondClass or ThirdClass, but not at classFirstClass.
        final Field propertyFromSecondTrait = PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyFromSecondTrait");
        Assert.assertNull(propertyFromSecondTrait);
        final Field propertyFromSecondTrait1 = PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromSecondTrait");
        Assert.assertNotNull(propertyFromSecondTrait1);
        Assert.assertEquals(traitSecondTrait, propertyFromSecondTrait1.getContainingClass());
        final Field propertyFromSecondTrait2 = PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromSecondTrait");
        Assert.assertNotNull(propertyFromSecondTrait2);
        Assert.assertEquals(traitSecondTrait, propertyFromSecondTrait2.getContainingClass());
    }

    public void testFindMethodDeclaration() {
        final PsiFile        fileSample  = getResourceFile("api/PhpClassUtil.findDeclaration.php");
        final List<PhpClass> fileClasses = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpClass.class));

        Assert.assertEquals(5, fileClasses.size());

        // Bogus assertions...
        final PhpClass classFirstClass  = fileClasses.get(2);
        final PhpClass classSecondClass = fileClasses.get(3);
        final PhpClass classThirdClass  = fileClasses.get(4);

        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classFirstClass, "methodInexistent"));
        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classSecondClass, "methodInexistent"));
        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classThirdClass, "methodInexistent"));

        // FirstClass have only methodFromFirstClass().
        final Method methodFromFirstClass = PhpClassUtil.findMethodDeclaration(classFirstClass, "methodFromFirstClass");
        Assert.assertNotNull(methodFromFirstClass);
        Assert.assertEquals(classFirstClass, methodFromFirstClass.getContainingClass());

        // SecondClass have both methodFromFirstClass() (from #1) and methodFromSecondClass().
        final Method methodFromFirstClass1 = PhpClassUtil.findMethodDeclaration(classSecondClass, "methodFromFirstClass");
        Assert.assertNotNull(methodFromFirstClass1);
        Assert.assertEquals(classFirstClass, methodFromFirstClass1.getContainingClass());
        final Method methodFromSecondClass = PhpClassUtil.findMethodDeclaration(classSecondClass, "methodFromSecondClass");
        Assert.assertNotNull(methodFromSecondClass);
        Assert.assertEquals(classSecondClass, methodFromSecondClass.getContainingClass());

        // SecondClass have all methodFromFirstClass() (from #1) and methodFromSecondClass() (from #2) and methodFromThirdClass().
        final Method methodFromFirstClass2 = PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromFirstClass");
        Assert.assertNotNull(methodFromFirstClass2);
        Assert.assertEquals(classFirstClass, methodFromFirstClass2.getContainingClass());
        final Method methodFromSecondClass1 = PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromSecondClass");
        Assert.assertNotNull(methodFromSecondClass1);
        Assert.assertEquals(classSecondClass, methodFromSecondClass1.getContainingClass());
        final Method methodFromThirdClass = PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromThirdClass");
        Assert.assertNotNull(methodFromThirdClass);
        Assert.assertEquals(classThirdClass, methodFromThirdClass.getContainingClass());
    }

    @NotNull
    private List<PhpClass> getPhpClasses() {
        final PsiFile        fileSample  = getResourceFile("api/PhpClassUtil.superClasses.php");
        final List<PhpClass> fileClasses = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpClass.class));

        Assert.assertEquals(4, fileClasses.size());

        return fileClasses;
    }
}
