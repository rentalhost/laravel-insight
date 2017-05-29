<?php

$directLiteral = 'directValue';

const INDIRECT_LITERAL = 'indirectValue';
$indirectLiteral = INDIRECT_LITERAL;

const WARPING_LITERAL = INDIRECT_LITERAL;
$warpingLiteral = WARPING_LITERAL;

$assignedLiteral = $withSubAssignment = $thirdLevel = WARPING_LITERAL;

$ccUnresolvedConstantReference = CC_UNRESOLVED_REFERENCE;

class ResolvingInsideClass
{
    const DIRECT_LITERAL = 'indirectClassValue';
    const INDIRECT_LITERAL = self::DIRECT_LITERAL;
    const WARPING_LITERAL = self::INDIRECT_LITERAL;

    public function __construct()
    {
        $indirectLiteral = self::INDIRECT_LITERAL;
        $warpingLiteral = self::WARPING_LITERAL;
    }
}

