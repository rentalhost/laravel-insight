<?php

// AssignmentExpression #0.
$directLiteral = 'directValue';

// AssignmentExpression #1.
const INDIRECT_LITERAL = 'indirectValue';
$indirectLiteral = INDIRECT_LITERAL;

// AssignmentExpression #2.
const WARPING_LITERAL = INDIRECT_LITERAL;
$warpingLiteral = WARPING_LITERAL;

// AssignmentExpression #3 and #4.
$assignedLiteral = $withSubAssignment = $thirdLevel = WARPING_LITERAL;

// AssignmentExpression #6.
$ccUnresolvedConstantReference = CC_UNRESOLVED_REFERENCE;

class ResolvingInsideClass
{
    const DIRECT_LITERAL = 'indirectClassValue';
    const INDIRECT_LITERAL = self::DIRECT_LITERAL;
    const WARPING_LITERAL = self::INDIRECT_LITERAL;

    public function __construct()
    {
        // AssignmentExpression #7 and #8.
        $indirectLiteral = self::INDIRECT_LITERAL;
        $warpingLiteral = self::WARPING_LITERAL;
    }
}

// AssignmentExpression #9.
const SHOULD_IGNORES_CYCLIC_LOOPINGS_A = SHOULD_IGNORES_CYCLIC_LOOPINGS_B;
const SHOULD_IGNORES_CYCLIC_LOOPINGS_B = SHOULD_IGNORES_CYCLIC_LOOPINGS_C;
const SHOULD_IGNORES_CYCLIC_LOOPINGS_C = SHOULD_IGNORES_CYCLIC_LOOPINGS_A;
$shouldAvoidCyclicLoopingsWithConstants = SHOULD_IGNORES_CYCLIC_LOOPINGS_A;

// AssignmentExpression #10.
$shouldAvoidCyclicLoopingsWithVariablesA = $shouldAvoidCyclicLoopingsWithVariablesB;
$shouldAvoidCyclicLoopingsWithVariablesB = $shouldAvoidCyclicLoopingsWithVariablesA;
