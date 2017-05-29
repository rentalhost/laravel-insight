package net.rentalhost.idea.api;

import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.AssignmentExpression;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.rentalhost.suite.FixtureSuite;

public class PhpExpressionUtilTest extends FixtureSuite {
    @NotNull
    private static PhpExpression getExpressionFromIndex(
        final List<AssignmentExpression> fileAssignments,
        final int index
    ) {
        return valueOf(PhpExpressionUtil.from((PhpExpression) valueOf(fileAssignments.get(index).getValue())));
    }

    public void testFrom() {
        final PsiFile                    fileSample      = getResourceFile("api/PhpExpressionUtil.warpingLiterals.php");
        final List<AssignmentExpression> fileAssignments = new ArrayList<>(PsiTreeUtil.findChildrenOfType(fileSample, AssignmentExpression.class));

        Assert.assertEquals(9, fileAssignments.size());

        Assert.assertEquals("directValue", ((StringLiteralExpression) getExpressionFromIndex(fileAssignments, 0)).getContents());

        Assert.assertEquals("indirectValue", ((StringLiteralExpression) getExpressionFromIndex(fileAssignments, 1)).getContents());
        Assert.assertEquals("indirectValue", ((StringLiteralExpression) getExpressionFromIndex(fileAssignments, 2)).getContents());
        Assert.assertEquals("indirectValue", ((StringLiteralExpression) getExpressionFromIndex(fileAssignments, 3)).getContents());
        Assert.assertEquals("indirectValue", ((StringLiteralExpression) getExpressionFromIndex(fileAssignments, 4)).getContents());

        Assert.assertNull(PhpExpressionUtil.from((PhpExpression) valueOf(fileAssignments.get(6).getValue())));
    }
}
