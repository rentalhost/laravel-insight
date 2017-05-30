<?php

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
