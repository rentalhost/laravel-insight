package net.rentalhost.idea.api;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.*;

import java.util.*;

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

    public static boolean hasSuperOfType(
        final PhpClass classObject,
        final String superNameExpected
    ) {
        return findSuperOfType(classObject, superNameExpected) != null;
    }

    @Nullable
    public static ClassReference findSuperOfType(
        final PhpClass classObject,
        final String superNameExpected
    ) {
        PhpClass classCurrent = classObject;

        while (true) {
            final ClassReference classSuperReference = getSuperReference(classCurrent);

            if (classSuperReference == null) {
                return null;
            }

            if (Objects.equals(classSuperReference.getFQN(), superNameExpected)) {
                return classSuperReference;
            }

            final PhpClass classSuperResolved = (PhpClass) classSuperReference.resolve();

            if (classSuperResolved == null) {
                return null;
            }

            classCurrent = classSuperResolved;
        }
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
    public static Field findPropertyDeclaration(
        final PhpClass classObject,
        final String propertyNameExpected
    ) {
        PhpClass classCurrent = classObject;

        while (true) {
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

            final PhpClass classSuperResolved = getSuper(classCurrent);

            if (classSuperResolved == null) {
                return null;
            }

            classCurrent = classSuperResolved;
        }
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
