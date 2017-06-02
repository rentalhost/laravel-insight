package net.rentalhost.idea.api;

import java.util.Stack;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

enum RecursionResolver {
    ;

    @Nullable
    public static <InputType, OutputType> OutputType resolve(
        @NotNull final InputType objectInitial,
        @NotNull final Function<Resolver, OutputType> resolver
    ) {
        return (new Resolver<>(resolver)).resolve(objectInitial);
    }

    public static class Resolver<InputType, OutputType> {
        @NotNull private final Stack<InputType>               objectStack;
        @NotNull private final Function<Resolver, OutputType> resolver;

        @Nullable private InputType object;

        Resolver(@NotNull final Function<Resolver, OutputType> objectResolver) {
            objectStack = new Stack<>();
            object = null;
            resolver = objectResolver;
        }

        @NotNull
        InputType getObject() {
            assert object != null;

            return object;
        }

        @Nullable
        OutputType resolve(@NotNull final InputType objectNext) {
            if (objectStack.contains(objectNext)) {
                return null;
            }

            objectStack.push(objectNext);
            object = objectNext;

            return resolver.apply(this);
        }
    }
}
