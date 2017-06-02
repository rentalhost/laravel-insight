<?php

class ObjectClass
{
    use FirstTrait;
    use SecondTrait,
        ThirdTrait;

    public function test()
    {
        return function () use ($shouldNotBeConsideredAsUseTrait) {
        };
    }
}
