<?php

trait FirstTraitOfTrait
{
}

trait SecondTraitOfTrait
{
    use FirstTraitOfTrait;
}

class FirstClassToTrait
{
    use SecondTraitOfTrait;
}
