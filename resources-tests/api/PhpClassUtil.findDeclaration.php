<?php

trait FirstPropertyOnTrait
{
    public $propertyFromFirstTrait;
}

trait SecondPropertyOnTrait
{
    public $propertyFromSecondTrait;
}

class FirstPropertyDeclarationClass
{
    use FirstPropertyOnTrait;
    use CC_UnresolvableTrait;

    public $propertyFromFirst;
}

class SecondPropertyDeclarationClass extends FirstPropertyDeclarationClass
{
    use SecondPropertyOnTrait;

    public $propertyFromSecond;
}

class ThirdPropertyDeclarationClass extends SecondPropertyDeclarationClass
{
    public $propertyFromThird;
}
