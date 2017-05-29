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

    public function methodFromFirstClass()
    {
    }
}

class SecondPropertyDeclarationClass extends FirstPropertyDeclarationClass
{
    use SecondPropertyOnTrait;

    public $propertyFromSecond;

    public function methodFromSecondClass()
    {
    }
}

class ThirdPropertyDeclarationClass extends SecondPropertyDeclarationClass
{
    public $propertyFromThird;

    public function methodFromThirdClass()
    {
    }
}
