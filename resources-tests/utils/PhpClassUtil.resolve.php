<?php

class Reference
{
}

(new Reference)->$newReference;

$directReference = new Reference();
$indirectReference = $directReference;
$indirectReference->$farReference;

(new UnresolvedReference)->$unresolvedReference;
