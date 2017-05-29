<?php

trait FirstPropertyOnTrait
{
    public $propertyFromFirstTrait;

    public function methodFromFirstTrait()
    {
    }
}

trait SecondPropertyOnTrait
{
    public $propertyFromSecondTrait;

    public function methodFromSecondTrait()
    {
    }
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

class TraitMethodAliased
{
    use FirstPropertyOnTrait {
        FirstPropertyOnTrait::methodFromFirstTrait as aliasedMethod;
    }
}
