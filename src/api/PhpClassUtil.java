package net.rentalhost.idea.api;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.PhpUseImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import org.jetbrains.annotations.Nullable;

public enum PhpClassUtil {
    ;

    public static Iterable<PhpUse> getTraitsDeclared(final PsiElement classObject) {
        assert classObject instanceof PhpClass;

        final List<PhpUseList> usesLists = PsiTreeUtil.getChildrenOfTypeAsList(classObject, PhpUseList.class);
        final Stack<PhpUse>    result    = new Stack<>();

        for (final PhpUseList useList : usesLists) {
            for (final PhpUse useDeclaration : useList.getDeclarations()) {
                if (useDeclaration.isTraitImport()) {
                    result.push(useDeclaration);
                }
            }
        }

        return result;
    }

    @Nullable
    public static ClassReference findSuperOfType(
        final PhpClass classObject,
        final String superNameExpected
    ) {
        PhpClass classCurrent = classObject;

        while (classCurrent != null) {
            final ClassReference classSuperReference = getSuperReference(classCurrent);

            if (classSuperReference == null) {
                return null;
            }

            if (Objects.equals(classSuperReference.getFQN(), superNameExpected)) {
                return classSuperReference;
            }

            classCurrent = (PhpClass) classSuperReference.resolve();
        }

        return null;
    }

    @Nullable
    public static ClassReference findTraitOfType(
        final PhpClass classObject,
        final String traitNameExpected
    ) {
        PhpClass classCurrent = classObject;

        while (classCurrent != null) {
            final Iterable<PhpUse> classTraits = getTraitsDeclared(classCurrent);

            for (final PhpUse classTrait : classTraits) {
                if (classTrait.getFQN().equals(traitNameExpected)) {
                    return (ClassReference) classTrait.getTargetReference();
                }

                final PhpReference traitTargetReference = classTrait.getTargetReference();
                assert traitTargetReference != null;

                final PhpClass traitResolved = (PhpClass) traitTargetReference.resolve();

                if (traitResolved == null) {
                    continue;
                }

                final ClassReference traitOfTrait = findTraitOfType(traitResolved, traitNameExpected);

                if (traitOfTrait == null) {
                    continue;
                }

                return traitOfTrait;
            }

            classCurrent = getSuper(classCurrent);
        }

        return null;
    }

    @Nullable
    public static ClassReference getSuperReference(final PhpClass phpClass) {
        final List<ClassReference> classExtendsList = phpClass.getExtendsList().getReferenceElements();

        if (classExtendsList.isEmpty()) {
            return null;
        }

        return classExtendsList.get(0);
    }

    @Nullable
    public static PhpClass getSuper(final PhpClass phpClass) {
        final ClassReference superReference = getSuperReference(phpClass);

        if (superReference == null) {
            return null;
        }

        return (PhpClass) superReference.resolve();
    }

    @Nullable
    public static PhpClass getTraitContainingClass(final PhpUse trait) {
        if (trait.isTraitImport()) {
            final PhpUseList useList = PhpUseImpl.getUseList(trait);
            assert useList != null;

            return (PhpClass) useList.getParent();
        }

        return null;
    }

    @Nullable
    public static Field findPropertyDeclaration(
        final PhpClass classObject,
        final String propertyNameExpected
    ) {
        PhpClass classCurrent = classObject;

        while (classCurrent != null) {
            final Collection<Field> classFields = classCurrent.getFields();

            for (final Field classField : classFields) {
                if (classField.getName().equals(propertyNameExpected)) {
                    return classField;
                }
            }

            for (final PhpUse classTrait : getTraitsDeclared(classCurrent)) {
                final PhpReference traitReference = classTrait.getTargetReference();
                assert traitReference != null;

                final PhpClass resolve = (PhpClass) traitReference.resolve();

                if (resolve == null) {
                    continue;
                }

                final Field traitDeclaration = findPropertyDeclaration(resolve, propertyNameExpected);

                if (traitDeclaration != null) {
                    return traitDeclaration;
                }
            }

            classCurrent = getSuper(classCurrent);
        }

        return null;
    }

    @Nullable
    public static Method findMethodDeclaration(
        final PhpClass classObject,
        final String methodNameExpected
    ) {
        PhpClass classCurrent = classObject;

        while (true) {
            final Collection<Method> classMethods = classCurrent.getMethods();

            for (final Method classMethod : classMethods) {
                if (classMethod.getName().equals(methodNameExpected)) {
                    return classMethod;
                }
            }

            for (final PhpTraitUseRule classTraitRule : classCurrent.getTraitUseRules()) {
                if (Objects.equals(classTraitRule.getAlias(), methodNameExpected)) {
                    final MethodReference classTraitReference = classTraitRule.getOriginalReference();

                    assert classTraitReference != null;

                    return (Method) classTraitReference.resolve();
                }

                final MethodReference classTraitRuleMethodScopedReference = (MethodReference) classTraitRule.getFirstPsiChild();
                assert classTraitRuleMethodScopedReference != null;

                final PsiReference classTraitRuleMethodClassReference = (PsiReference) classTraitRuleMethodScopedReference.getFirstPsiChild();
                assert classTraitRuleMethodClassReference != null;

                final PsiElement classTraitRuleMethodClass = classTraitRuleMethodClassReference.resolve();

                if (classTraitRuleMethodClass == null) {
                    continue;
                }

                final Method classTraitRuleMethod = findMethodDeclaration((PhpClass) classTraitRuleMethodClass, methodNameExpected);

                if (classTraitRuleMethod == null) {
                    continue;
                }

                return classTraitRuleMethod;
            }

            for (final PhpUse classTrait : getTraitsDeclared(classCurrent)) {
                final PhpReference traitReference = classTrait.getTargetReference();
                assert traitReference != null;

                final PhpClass resolve = (PhpClass) traitReference.resolve();

                if (resolve == null) {
                    continue;
                }

                final Method traitDeclaration = findMethodDeclaration(resolve, methodNameExpected);

                if (traitDeclaration != null) {
                    return traitDeclaration;
                }
            }

            final PhpClass classSuperResolved = getSuper(classCurrent);

            if (classSuperResolved == null) {
                return null;
            }

            classCurrent = classSuperResolved;
        }
    }
}
