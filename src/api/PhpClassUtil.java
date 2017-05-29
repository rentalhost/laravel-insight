package net.rentalhost.idea.api;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

public enum PhpClassUtil {
    ;

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

            final PhpClass classSuperResolved = getSuper(classCurrent);

            if (classSuperResolved == null) {
                return null;
            }

            classCurrent = classSuperResolved;
        }
    }
}
