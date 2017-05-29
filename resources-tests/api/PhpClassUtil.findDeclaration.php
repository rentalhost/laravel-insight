<?php

trait FirstPropertyOnTrait
{
    public $propertyFromFirstTrait;

    public function methodFromFirstTrait()
    {
    }

    public function methodFromBothTraits()
    {
    }

    public function secondMethodFromBothTraits()
    {
    }
}

trait SecondPropertyOnTrait
{
    public $propertyFromSecondTrait;

    public function methodFromSecondTrait()
    {
    }

    public function methodFromBothTraits()
    {
    }

    public function secondMethodFromBothTraits()
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

class TraitMethodWithInsteadof
{
    use FirstPropertyOnTrait,
        SecondPropertyOnTrait {
        SecondPropertyOnTrait::methodFromBothTraits insteadof FirstPropertyOnTrait;
        SecondPropertyOnTrait::secondMethodFromBothTraits insteadof FirstPropertyOnTrait;
        FirstPropertyOnTrait::secondMethodFromBothTraits as recoveredMethodFromBothTraits;

        CC_UnresolvedReferenceClass::CC_ignoredMethodReference insteadof CC_IgnoredClassReference;
    }
}
