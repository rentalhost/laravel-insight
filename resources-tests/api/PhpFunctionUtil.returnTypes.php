<?php

use MyNamespace\ResolvableQualifier;

/** @return string */
function respectPhpdocReturnType_StringOnly()
{
}

/** @return string|null */
function respectPhpdocReturnType_StringOrNull()
{
}

/** @return null|string */
function respectPhpdocReturnType_NullOrString()
{
}

/** @return string|int|boolean|float */
function respectPhpdocReturnType_AllScalarTypes()
{
}

/** @return UnresolvableQualifier */
function respectPhpdocReturnType_UnresolvableQualifier()
{
}

/** @return ResolvableQualifier */
function respectPhpdocReturnType_ResolvableQualifier()
{
}

function respectReturnType_SingularType(): int
{
}

function respectReturnType_SingularNullableType(): ?int
{
}

function respectReturnType_UnresolvableQualifierType(): UnresolvableQualifier
{
}

function respectReturnType_UnresolvableQualifierNullableType(): ?UnresolvableQualifier
{
}

/** @return string (ignored) */
function respectReturnType_CCShouldIgnoresPhpdoc(): int
{
}

function respectReturnType_ResolvableQualifier(): ResolvableQualifier
{
}

function respectNewReturnType_UnresolvableQualifier()
{
    return new UnresolvableQualifier();
}

function respectNewReturnType_ResolvableQualifier()
{
    return new ResolvableQualifier();
}

function respectNewReturnType_ABCQualifiers()
{
    return new A();
    return new B();
    return new C();
}

function respectClosureReturnType()
{
    return function () {
    };
}

function respectClosureReturnType_shouldIgnoresInnerReturnType()
{
    return function () {
        return new ShouldIgnoreThat();
    };
}

function returnResolvableQualifier()
{
    return new ResolvableQualifier();
}

function respectIndirectReturnType()
{
    return returnResolvableQualifier();
}

function respectVariableTypeOnReturn()
{
    $variable = 'string';
    return $variable;
}

class ThisQualifier
{
    public function respectThisTypeOnReturn()
    {
        return $this;
    }
}

class SelfQualifier
{
    public function respectSelfQualifierOnReturn(): self
    {
    }
}

function unifyDuplicatedReturnTypes()
{
    return new ResolvableQualifier;
    return new ResolvableQualifier;
}

class ChainSimulator
{
    public function chain()
    {
        return $this;
    }
}

function respectChainedReturnType_singleCall_fromNew()
{
    return (new ChainSimulator())
        ->chain();
}

function respectChainedReturnType_multiCall_fromVariable()
{
    $chainSimulator = new ChainSimulator();
    return $chainSimulator
        ->chain()->chain()->chain()->chain()->chain()
        ->chain()->chain()->chain()->chain()->chain();
}

function respectMixedReturnType_fromUnresolvableReference_fromVariable()
{
    return $unresolvableVariable;
}

function respectMixedReturnType_fromUnresolvableReference_fromFunction()
{
    return unresolvableFunction();
}

function avoidInfinityLoopingA_mixedType()
{
    return avoidInfinityLoopingB_mixedType();
}

function avoidInfinityLoopingB_mixedType()
{
    return avoidInfinityLoopingC_mixedType();
}

function avoidInfinityLoopingC_mixedType()
{
    return avoidInfinityLoopingA_mixedType();
}

/** @return string (should be respected over mixed) */
function avoidInfinityLoopingA_respectPhpdoc()
{
    return avoidInfinityLoopingB_respectPhpdoc();
}

function avoidInfinityLoopingB_respectPhpdoc()
{
    return avoidInfinityLoopingC_respectPhpdoc();
}

function avoidInfinityLoopingC_respectPhpdoc()
{
    return avoidInfinityLoopingA_respectPhpdoc();
}
