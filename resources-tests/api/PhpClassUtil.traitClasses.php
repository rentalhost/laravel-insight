<?php

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
