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

    const RESOLVING_PRIMARY_CONTANTS = TRUE;
    protected $resolvingFromProperty = self::RESOLVING_PRIMARY_CONTANTS;

    protected $resolvingDirectlyFromProperty = null;

    public function __construct()
    {
        $classIndirectLiteral = self::INDIRECT_LITERAL;
        $classWarpingLiteral = self::WARPING_LITERAL;
    }

    public static function shouldNotResolveTotally()
    {
    }
}

const SHOULD_IGNORES_CYCLIC_LOOPINGS_A = SHOULD_IGNORES_CYCLIC_LOOPINGS_B;
const SHOULD_IGNORES_CYCLIC_LOOPINGS_B = SHOULD_IGNORES_CYCLIC_LOOPINGS_C;
const SHOULD_IGNORES_CYCLIC_LOOPINGS_C = SHOULD_IGNORES_CYCLIC_LOOPINGS_A;
$shouldAvoidCyclicLoopingsWithConstants = SHOULD_IGNORES_CYCLIC_LOOPINGS_A;

$shouldAvoidCyclicLoopingsWithVariablesA = $shouldAvoidCyclicLoopingsWithVariablesB;
$shouldAvoidCyclicLoopingsWithVariablesB = $shouldAvoidCyclicLoopingsWithVariablesA;

$variableDirect = "value";
$variableIndirect = $variableDirect;
$variableWrapping = $variableIndirect;

$withParanteshesDirect = (("parentheses"));
$withParanteshesIndirect = (($withParanteshesDirect));
$withParanteshesWrapping = (($withParanteshesIndirect));

$shouldNotResolveTotally = Reference::shouldNotResolveTotally();
$indirectShouldNotResolveTotally = $shouldNotResolveTotally;

const SHOULD_STOP_HERE = NOT_HERE;
const NOT_HERE = false;
$stopOnFirstConstantReference = SHOULD_STOP_HERE;
