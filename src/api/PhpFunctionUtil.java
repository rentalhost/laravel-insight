package net.rentalhost.idea.api;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpReturnInstruction;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.NewExpression;
import com.jetbrains.php.lang.psi.elements.PhpReference;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

public enum PhpFunctionUtil {
    ;

    @Nullable
    public static PhpType getReturnType(final Function function) {
        final PsiElement functionReturnType = function.getReturnType();

        if (functionReturnType instanceof PhpReference) {
            final String                 functionReturnTypeFQN     = ((PhpReference) functionReturnType).getFQN();
            final PhpType.PhpTypeBuilder functionReturnTypePrimary = PhpType.builder().add(functionReturnTypeFQN);

            final PsiElement prevMatch = TreeUtil.getPrevMatch(
                functionReturnType,
                filterBy -> (filterBy instanceof ASTNode) && Objects.equals(((ASTNode) filterBy).getElementType(), PhpTokenTypes.opQUEST),
                stopBy -> (stopBy instanceof ASTNode) && Objects.equals(((ASTNode) stopBy).getElementType(), PhpTokenTypes.chRPAREN)
            );

            if (prevMatch instanceof ASTNode) {
                functionReturnTypePrimary.add(PhpType.NULL);
            }

            return functionReturnTypePrimary.build();
        }

        final PhpInstruction[] phpInstructions = function.getControlFlow().getInstructions();
        if (phpInstructions.length != 0) {
            final PhpType.PhpTypeBuilder functionReturnTypes = PhpType.builder();

            for (final PhpInstruction phpInstruction : phpInstructions) {
                if (phpInstruction instanceof PhpReturnInstruction) {
                    final PsiElement phpInstructionArgument = ((PhpReturnInstruction) phpInstruction).getArgument();

                    if (phpInstructionArgument instanceof NewExpression) {
                        final ClassReference phpInstructionClassReference = ((NewExpression) phpInstructionArgument).getClassReference();
                        assert phpInstructionClassReference != null;

                        functionReturnTypes.add(phpInstructionClassReference.getFQN());
                    }
                }
            }

            final PhpType functionReturnTypesBuilded = functionReturnTypes.build();

            if (!functionReturnTypesBuilded.isEmpty()) {
                return functionReturnTypesBuilded;
            }
        }

        final PhpDocComment functionDocComment = function.getDocComment();

        if (functionDocComment != null) {
            final PhpDocReturnTag functionDocReturnTag = functionDocComment.getReturnTag();

            if (functionDocReturnTag != null) {
                return functionDocReturnTag.getType();
            }
        }

        return null;
    }
}
