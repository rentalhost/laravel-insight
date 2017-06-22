<?php

class Reference
{
    public function getSelf(): ?self
    {
        return $this;
    }
}

(new Reference)->$newReference;

$directReference = new Reference();
$indirectReference = $directReference;
$indirectReference->$farReference;
$indirectReference->getSelf()->$chainedReference;

(new UnresolvedReference)->$unresolvedReference;
