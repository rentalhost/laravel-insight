package net.rentalhost.idea.api;

import java.util.Stack;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

enum RecursionResolver {
    ;

    @Nullable
    public static <InputType, OutputType> OutputType resolve(
        final InputType objectInitial,
        final Function<Resolver, OutputType> resolver
    ) {
        return (new Resolver<>(resolver)).resolve(objectInitial);
    }

    public static class Resolver<InputType, OutputType> {
        private final Stack<InputType>               objectStack;
        private final Function<Resolver, OutputType> resolver;

        private InputType object;

        Resolver(final Function<Resolver, OutputType> objectResolver) {
            objectStack = new Stack<>();
            object = null;
            resolver = objectResolver;
        }

        InputType getObject() {
            assert object != null;

            return object;
        }

        @Nullable
        OutputType resolve(final InputType objectNext) {
            if (objectStack.contains(objectNext)) {
                return null;
            }

            objectStack.push(objectNext);
            object = objectNext;

            return resolver.apply(this);
        }
    }
}
