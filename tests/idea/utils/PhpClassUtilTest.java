package net.rentalhost.idea.utils;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;

import net.rentalhost.suite.FixtureSuite;

public class PhpClassUtilTest extends FixtureSuite {
    public void testGetTraitsDeclared() {
        final PsiFile fileSample = getResourceFile("utils/PhpClassUtil.traitsDeclaration.php");

        final ArrayList<PhpUse> phpUses = Lists.newArrayList(PhpClassUtil.getTraitsDeclared(valueOf(getElementByName(fileSample, "ObjectClass"))));

        Assert.assertSame(3, phpUses.size());

        Assert.assertEquals("\\FirstTrait", valueOf(getElementByName(fileSample, "FirstTrait")).getFQN());
        Assert.assertEquals("\\SecondTrait", valueOf(getElementByName(fileSample, "SecondTrait")).getFQN());
        Assert.assertEquals("\\ThirdTrait", valueOf(getElementByName(fileSample, "ThirdTrait")).getFQN());
    }

    public void testFindSuperOfType() {
        final PsiFile fileSample = getResourceFile("utils/PhpClassUtil.superClasses.php");

        final PhpClass classFirstClass  = (PhpClass) getElementByName(fileSample, "FirstClass");
        final PhpClass classSecondClass = (PhpClass) getElementByName(fileSample, "SecondClass");
        final PhpClass classThirdClass  = (PhpClass) getElementByName(fileSample, "ThirdClass");

        // Bogus assertions...
        Assert.assertNull(PhpClassUtil.findSuperOfType(classFirstClass, "\\ThisClassDontHaveParent"));
        Assert.assertNull(PhpClassUtil.findSuperOfType(classSecondClass, "\\ThisDoesButNotThat"));
        Assert.assertNull(PhpClassUtil.findSuperOfType(classThirdClass, "\\ThisDoesButNotThat"));

        // FirstClass is parent of SecondClass.
        Assert.assertNotNull(PhpClassUtil.findSuperOfType(classSecondClass, "\\FirstClass"));

        // FirstClass and SecondClass are parent of ThirdClass.
        Assert.assertNotNull(PhpClassUtil.findSuperOfType(classThirdClass, "\\SecondClass"));
        Assert.assertNotNull(PhpClassUtil.findSuperOfType(classThirdClass, "\\FirstClass"));

        final PhpClass classCC_ChildClass = (PhpClass) getElementByName(fileSample, "CC_ChildClass");

        // CC_UnresolvableParentClass is parent of CC_ChildClass (case #1).
        // CC_UnresolvableParentClass is unresolvable, then we can't try search after that (case #2).
        Assert.assertNotNull(PhpClassUtil.findSuperOfType(classCC_ChildClass, "\\CC_UnresolvableParentClass"));
        Assert.assertNull(PhpClassUtil.findSuperOfType(classCC_ChildClass, "\\CC_ThereIsNothingAfterUnresolvableParentClass"));
    }

    public void testFindTraitOfType() {
        final PsiFile fileSample = getResourceFile("utils/PhpClassUtil.traitClasses.php");

        final PhpClass traitFirstTrait               = (PhpClass) getElementByName(fileSample, "FirstTrait");
        final PhpClass traitSecondTrait              = (PhpClass) getElementByName(fileSample, "SecondTrait");
        final PhpClass classFirstClass               = (PhpClass) getElementByName(fileSample, "FirstClass");
        final PhpClass classSecondClass              = (PhpClass) getElementByName(fileSample, "SecondClass");
        final PhpClass classCCUnresolvableTraitClass = (PhpClass) getElementByName(fileSample, "CC_UnresolvableTraitClass");

        Assert.assertNull(PhpClassUtil.findTraitOfType(traitFirstTrait, "\\ThisTraitDoesntExists"));
        Assert.assertNull(PhpClassUtil.findTraitOfType(traitSecondTrait, "\\ThisTraitDoesntExists"));
        Assert.assertNull(PhpClassUtil.findTraitOfType(classFirstClass, "\\ThisTraitDoesntExists"));
        Assert.assertNull(PhpClassUtil.findTraitOfType(classSecondClass, "\\ThisTraitDoesntExists"));

        // FirstClass have FirstTrait.
        Assert.assertSame(traitFirstTrait, valueOf(PhpClassUtil.findTraitOfType(classFirstClass, "\\FirstTrait")).resolve());

        // SecondClass have FirstTrait and SecondTrait.
        Assert.assertSame(traitFirstTrait, valueOf(PhpClassUtil.findTraitOfType(classSecondClass, "\\FirstTrait")).resolve());
        Assert.assertSame(traitSecondTrait, valueOf(PhpClassUtil.findTraitOfType(classSecondClass, "\\SecondTrait")).resolve());

        // Code-coverage.
        Assert.assertNull(PhpClassUtil.findTraitOfType(classCCUnresolvableTraitClass, "\\Anything"));

        final PsiFile              fileSample2  = getResourceFile("utils/PhpClassUtil.traitOfTrait.php");
        final Collection<PhpClass> fileClasses2 = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample2, PhpClass.class));

        Assert.assertEquals(3, fileClasses2.size());

        final PhpClass traitFirstTraitOfTrait  = (PhpClass) getElementByName(fileSample2, "FirstTraitOfTrait");
        final PhpClass traitSecondTraitOfTrait = (PhpClass) getElementByName(fileSample2, "SecondTraitOfTrait");
        final PhpClass classFirstClassToTrait  = (PhpClass) getElementByName(fileSample2, "FirstClassToTrait");

        // FirstClassToTrait have both FirstTraitOfTrait and SecondTraitOfTrait.
        Assert.assertSame(traitFirstTraitOfTrait, valueOf(PhpClassUtil.findTraitOfType(classFirstClassToTrait, "\\FirstTraitOfTrait")).resolve());
        Assert.assertSame(traitSecondTraitOfTrait, valueOf(PhpClassUtil.findTraitOfType(classFirstClassToTrait, "\\SecondTraitOfTrait")).resolve());

        final PsiFile              fileSample3  = getResourceFile("utils/PhpClassUtil.namespacedTrait.php");
        final Collection<PhpClass> fileClasses3 = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample3, PhpClass.class));

        Assert.assertEquals(1, fileClasses3.size());

        final PhpClass classTestClass = (PhpClass) getElementByName(fileSample3, "TestClass");

        // Check if namespaced FQN is right.
        Assert.assertEquals("\\Namespaced\\TraitClass", valueOf(PhpClassUtil.findTraitOfType(classTestClass, "\\Namespaced\\TraitClass")).getFQN());
    }

    public void testGetSuperReference() {
        final PsiFile fileSample = getResourceFile("utils/PhpClassUtil.superClasses.php");

        // FirstClass have no super class.
        Assert.assertNull(PhpClassUtil.getSuperReference((PhpClass) getElementByName(fileSample, "FirstClass")));

        // FirstClass is the super class of SecondClass.
        // Checking just one level, because is not a recursive method.
        Assert.assertEquals("\\FirstClass", valueOf(PhpClassUtil.getSuperReference((PhpClass) getElementByName(fileSample, "SecondClass"))).getFQN());
    }

    public void testGetSuper() {
        final PsiFile fileSample = getResourceFile("utils/PhpClassUtil.superClasses.php");

        final PhpClass classFirstClass  = (PhpClass) getElementByName(fileSample, "FirstClass");
        final PhpClass classSecondClass = (PhpClass) getElementByName(fileSample, "SecondClass");

        // FirstClass have no super class.
        Assert.assertNull(PhpClassUtil.getSuper(classFirstClass));

        // FirstClass is the super class of SecondClass.
        // Checking just one level, because is not a recursive method.
        Assert.assertEquals(classFirstClass, PhpClassUtil.getSuper(classSecondClass));
    }

    public void testFindPropertyDeclaration() {
        final PsiFile fileSample = getResourceFile("utils/PhpClassUtil.findDeclaration.php");

        final PhpClass classFirstClass  = (PhpClass) getElementByName(fileSample, "FirstPropertyDeclarationClass");
        final PhpClass classSecondClass = (PhpClass) getElementByName(fileSample, "SecondPropertyDeclarationClass");
        final PhpClass classThirdClass  = (PhpClass) getElementByName(fileSample, "ThirdPropertyDeclarationClass");

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

        final PhpClass traitFirstTrait  = (PhpClass) getElementByName(fileSample, "FirstPropertyOnTrait");
        final PhpClass traitSecondTrait = (PhpClass) getElementByName(fileSample, "SecondPropertyOnTrait");

        // $firstPropertyOnTrait should be detected from FirstClass, SecondClass or ThirdClass.
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyFromFirstTrait")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromFirstTrait")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromFirstTrait")).getContainingClass());

        // $secondPropertyOnTrait should be detected from SecondClass or ThirdClass, but not at classFirstClass.
        Assert.assertNull(PhpClassUtil.findPropertyDeclaration(classFirstClass, "propertyFromSecondTrait"));
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classSecondClass, "propertyFromSecondTrait")).getContainingClass());
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findPropertyDeclaration(classThirdClass, "propertyFromSecondTrait")).getContainingClass());

        final PhpClass classCyclicClassA = (PhpClass) getElementByName(fileSample, "CyclicClassA");

        Assert.assertNull(PhpClassUtil.findPropertyDeclaration(classCyclicClassA, "inexistentProperty"));

        // Code-coverage:
        final PhpClass classCCPropertyFromTraitClass = (PhpClass) getElementByName(fileSample, "CC_PropertyFromTraitClass");
        final PhpClass traitCCPropertyFromTrait      = (PhpClass) getElementByName(fileSample, "CC_PropertyFromTrait");

        Assert.assertEquals(traitCCPropertyFromTrait,
                            valueOf(PhpClassUtil.findPropertyDeclaration(classCCPropertyFromTraitClass, "propertyFromTrait")).getContainingClass());
    }

    public void testFindMethodDeclaration() {
        final PsiFile fileSample = getResourceFile("utils/PhpClassUtil.findDeclaration.php");

        final PhpClass classFirstClass  = (PhpClass) getElementByName(fileSample, "FirstPropertyDeclarationClass");
        final PhpClass classSecondClass = (PhpClass) getElementByName(fileSample, "SecondPropertyDeclarationClass");
        final PhpClass classThirdClass  = (PhpClass) getElementByName(fileSample, "ThirdPropertyDeclarationClass");

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

        final PhpClass traitFirstTrait  = (PhpClass) getElementByName(fileSample, "FirstPropertyOnTrait");
        final PhpClass traitSecondTrait = (PhpClass) getElementByName(fileSample, "SecondPropertyOnTrait");

        // methodFromFirstTrait() should be detected from FirstClass, SecondClass or ThirdClass.
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classFirstClass, "methodFromFirstTrait")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classSecondClass, "methodFromFirstTrait")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromFirstTrait")).getContainingClass());

        // methodFromSecondTrait() should be detected from SecondClass or ThirdClass, but not at classFirstClass.
        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classFirstClass, "methodFromSecondTrait"));
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findMethodDeclaration(classSecondClass, "methodFromSecondTrait")).getContainingClass());
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findMethodDeclaration(classThirdClass, "methodFromSecondTrait")).getContainingClass());

        final PhpClass classTraitMethodAliased = (PhpClass) getElementByName(fileSample, "TraitMethodAliased");

        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classTraitMethodAliased, "aliasedMethod")).getContainingClass());

        final PhpClass classTraitMethodWithInsteadofClass = (PhpClass) getElementByName(fileSample, "TraitMethodWithInsteadof");

        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findMethodDeclaration(classTraitMethodWithInsteadofClass, "methodFromBothTraits")).getContainingClass());
        Assert.assertEquals(traitSecondTrait, valueOf(PhpClassUtil.findMethodDeclaration(classTraitMethodWithInsteadofClass, "secondMethodFromBothTraits")).getContainingClass());
        Assert.assertEquals(traitFirstTrait, valueOf(PhpClassUtil.findMethodDeclaration(classTraitMethodWithInsteadofClass, "recoveredMethodFromBothTraits")).getContainingClass());
        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classTraitMethodWithInsteadofClass, "CC_unresolvedReference"));

        final PhpClass classCyclicClassA = (PhpClass) getElementByName(fileSample, "CyclicClassA");

        Assert.assertNull(PhpClassUtil.findMethodDeclaration(classCyclicClassA, "inexistentMethod"));
    }

    public void testGetTraitContainingClass() {
        final PsiFile fileSample = getResourceFile("utils/PhpClassUtil.traitClasses.php");

        final PsiElement classFirstClass    = getElementByName(fileSample, "FirstClass");
        final PhpUse     firstClassTraitUse = valueOf(PsiTreeUtil.findChildOfType(classFirstClass, PhpUse.class));

        Assert.assertNotNull(PhpClassUtil.getTraitContainingClass(firstClassTraitUse));

        final PhpUse useImport = (PhpUse) valueOf(getElementByName(fileSample, "CC_NotIsATrait_APIConsiderTooAsUse"));

        Assert.assertNull(PhpClassUtil.getTraitContainingClass(useImport));
    }
}
