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

    public void testFindTraitOfType() {
        final PsiFile        fileSample  = getResourceFile("api/PhpClassUtil.traitClasses.php");
        final List<PhpClass> fileClasses = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpClass.class));

        Assert.assertEquals(4, fileClasses.size());

        final PhpClass traitFirstTrait  = fileClasses.get(0);
        final PhpClass traitSecondTrait = fileClasses.get(1);
        final PhpClass classFirstClass  = fileClasses.get(2);
        final PhpClass classSecondClass = fileClasses.get(3);

        Assert.assertNull(PhpClassUtil.findTraitOfType(traitFirstTrait, "\\ThisTraitDoesntExists"));
        Assert.assertNull(PhpClassUtil.findTraitOfType(traitSecondTrait, "\\ThisTraitDoesntExists"));
        Assert.assertNull(PhpClassUtil.findTraitOfType(classFirstClass, "\\ThisTraitDoesntExists"));
        Assert.assertNull(PhpClassUtil.findTraitOfType(classSecondClass, "\\ThisTraitDoesntExists"));

        // FirstClass have FirstTrait.
        Assert.assertSame(traitFirstTrait, valueOf(PhpClassUtil.findTraitOfType(classFirstClass, "\\FirstTrait")).resolve());

        // SecondClass have FirstTrait and SecondTrait.
        Assert.assertSame(traitFirstTrait, valueOf(PhpClassUtil.findTraitOfType(classSecondClass, "\\FirstTrait")).resolve());
        Assert.assertSame(traitSecondTrait, valueOf(PhpClassUtil.findTraitOfType(classSecondClass, "\\SecondTrait")).resolve());
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
        Assert.assertEquals("\\FirstClass", valueOf(PhpClassUtil.getSuperReference(fileClasses.get(1))).getFQN());
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

        Assert.assertEquals(7, fileClasses.size());

        final PhpClass classFirstClass  = fileClasses.get(2);
        final PhpClass classSecondClass = fileClasses.get(3);
        final PhpClass classThirdClass  = fileClasses.get(4);

        // Bogus assertions...
        Assert.assertNull(PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyInexistent"));
        Assert.assertNull(PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyInexistent"));
        Assert.assertNull(PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyInexistent"));

        // FirstClass have only $propertyFromFirst.
        Assert.assertEquals(classFirstClass, valueOf(PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyFromFirst")).getContainingClass());

        // SecondClass have both $propertyFromFirst (from #1) and $propertyFromSecond.
        Assert.assertEquals(classFirstClass, valueOf(PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromFirst")).getContainingClass());
        Assert.assertEquals(classSecondClass, valueOf(PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromSecond")).getContainingClass());

        // SecondClass have all $propertyFromFirst (from #1) and $propertyFromSecond (from #2) and $propertyFromThird.
        Assert.assertEquals(classFirstClass, valueOf(PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromFirst")).getContainingClass());
        Assert.assertEquals(classSecondClass, valueOf(PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromSecond")).getContainingClass());
        Assert.assertEquals(classThirdClass, valueOf(PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromThird")).getContainingClass());

        final PhpClass traitFirstTrait  = fileClasses.get(0);
        final PhpClass traitSecondTrait = fileClasses.get(1);

        // $firstPropertyOnTrait should be detected from FirstClass, SecondClass or ThirdClass.
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyFromFirstTrait")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromFirstTrait")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromFirstTrait")).getContainingClass());

        // $secondPropertyOnTrait should be detected from SecondClass or ThirdClass, but not at classFirstClass.
        Assert.assertNull(PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyFromSecondTrait"));
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromSecondTrait")).getContainingClass());
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromSecondTrait")).getContainingClass());
    }

    public void testFindMethodDeclaration() {
        final PsiFile        fileSample  = getResourceFile("api/PhpClassUtil.findDeclaration.php");
        final List<PhpClass> fileClasses = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpClass.class));

        Assert.assertEquals(7, fileClasses.size());

        final PhpClass classFirstClass  = fileClasses.get(2);
        final PhpClass classSecondClass = fileClasses.get(3);
        final PhpClass classThirdClass  = fileClasses.get(4);

        // Bogus assertions...
        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classFirstClass, "methodInexistent"));
        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classSecondClass, "methodInexistent"));
        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classThirdClass, "methodInexistent"));

        // FirstClass have only methodFromFirstClass().
        Assert.assertEquals(classFirstClass, valueOf(PhpClassUtil.findMethodDeclaration(classFirstClass, "methodFromFirstClass")).getContainingClass());

        // SecondClass have both methodFromFirstClass() (from #1) and methodFromSecondClass().
        Assert.assertEquals(classFirstClass, valueOf(PhpClassUtil.findMethodDeclaration(classSecondClass, "methodFromFirstClass")).getContainingClass());
        Assert.assertEquals(classSecondClass, valueOf(PhpClassUtil.findMethodDeclaration(classSecondClass, "methodFromSecondClass")).getContainingClass());

        // SecondClass have all methodFromFirstClass() (from #1) and methodFromSecondClass() (from #2) and methodFromThirdClass().
        Assert.assertEquals(classFirstClass, valueOf(PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromFirstClass")).getContainingClass());
        Assert.assertEquals(classSecondClass, valueOf(PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromSecondClass")).getContainingClass());
        Assert.assertEquals(classThirdClass, valueOf(PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromThirdClass")).getContainingClass());

        final PhpClass traitFirstTrait  = fileClasses.get(0);
        final PhpClass traitSecondTrait = fileClasses.get(1);

        // methodFromFirstTrait() should be detected from FirstClass, SecondClass or ThirdClass.
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classFirstClass, "methodFromFirstTrait")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classSecondClass, "methodFromFirstTrait")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromFirstTrait")).getContainingClass());

        // methodFromSecondTrait() should be detected from SecondClass or ThirdClass, but not at classFirstClass.
        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classFirstClass, "methodFromSecondTrait"));
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findMethodDeclaration(classSecondClass, "methodFromSecondTrait")).getContainingClass());
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromSecondTrait")).getContainingClass());

        final PhpClass classTraitMethodAliased = fileClasses.get(5);

        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classTraitMethodAliased, "aliasedMethod")).getContainingClass());

        final PhpClass classTraitMethodWithInsteadofClass = fileClasses.get(6);

        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findMethodDeclaration(classTraitMethodWithInsteadofClass, "methodFromBothTraits")).getContainingClass());
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findMethodDeclaration(classTraitMethodWithInsteadofClass, "secondMethodFromBothTraits")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classTraitMethodWithInsteadofClass, "recoveredMethodFromBothTraits")).getContainingClass());
        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classTraitMethodWithInsteadofClass, "CC_unresolvedReference"));
    }

    public void testGetTraitContainingClass() {
        final PsiFile      fileSample    = getResourceFile("api/PhpClassUtil.traitClasses.php");
        final List<PhpUse> fileTraitUses = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpUse.class));

        Assert.assertEquals(2, fileTraitUses.size());

        final PhpUse firstClassTraitUse = fileTraitUses.get(0);

        Assert.assertNotNull(PhpClassUtil.getTraitContainingClass(firstClassTraitUse));
    }

    @NotNull
    private List<PhpClass> getPhpClasses() {
        final PsiFile        fileSample  = getResourceFile("api/PhpClassUtil.superClasses.php");
        final List<PhpClass> fileClasses = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, PhpClass.class));

        Assert.assertEquals(4, fileClasses.size());

        return fileClasses;
    }
}
