<?php

use CC_NotIsATrait_APIConsiderTooAsUse;

trait FirstTrait
{
}

trait SecondTrait
{
}

class FirstClass
{
    use FirstTrait;
}

class SecondClass extends FirstClass
{
    use SecondTrait;
}

class CC_UnresolvableTraitClass
{
    use UnresolvableTrait;
}
