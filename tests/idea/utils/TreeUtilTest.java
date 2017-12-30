package net.rentalhost.idea.utils;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpReturnType;
import org.junit.Assert;

import net.rentalhost.suite.FixtureSuite;

public class TreeUtilTest extends FixtureSuite {
    public void testGetPrevMatch() {
        final PsiFile fileSample = getResourceFile("utils/TreeUtil.elements.php");

        Assert.assertNull(TreeUtil.getPrevMatch(
            getElementByName(fileSample, "referenceVariable"),
            filterBy -> false,
            stopBy -> false
        ));

        Assert.assertNull(TreeUtil.getPrevMatch(
            getElementByName(fileSample, "referenceVariable"),
            filterBy -> false,
            stopBy -> true
        ));

        final Function      referenceFunction   = (Function) getElementByName(fileSample, "referenceFunction");
        final PhpReturnType returnType          = referenceFunction.getReturnType();
        final PsiElement    referenceReturnType = valueOf((returnType != null) ? returnType.getClassReference() : null);

        Assert.assertNull(TreeUtil.getPrevMatch(
            referenceReturnType,
            filterBy -> (filterBy instanceof ASTNode) && filterBy.equals(PhpTokenTypes.opQUEST),
            PhpDocComment.class::isInstance
        ));

        Assert.assertEquals(PhpTokenTypes.opQUEST,
                            valueOf(((ASTNode) TreeUtil.getPrevMatch(
                                referenceReturnType,
                                filterBy -> (filterBy instanceof ASTNode) && ((ASTNode) filterBy).getElementType().equals(PhpTokenTypes.opQUEST),
                                stopBy -> false
                            ))).getElementType());
    }
}
