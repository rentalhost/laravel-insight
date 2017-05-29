package net.rentalhost.idea.api;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;

import java.util.List;
import java.util.Objects;

public enum PhpClassUtil {
    ;

    public static boolean hasSuperOfType(
        final PhpClass classObject,
        final String superNameExpected
    ) {
        return findSuperOfType(classObject, superNameExpected) != null;
    }

    public static ClassReference findSuperOfType(
        final PhpClass classObject,
        final String superNameExpected
    ) {
        PhpClass classCurrent = classObject;

        while (true) {
            final List<ClassReference> classExtendsList = classCurrent.getExtendsList().getReferenceElements();

            if (classExtendsList.isEmpty()) {
                return null;
            }

            final ClassReference classSuperReference = classExtendsList.get(0);

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
}
