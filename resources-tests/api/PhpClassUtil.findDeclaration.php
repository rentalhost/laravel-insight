<?php

class FirstPropertyDeclarationClass
{
    public $propertyFromFirst;
}

class SecondPropertyDeclarationClass extends FirstPropertyDeclarationClass
{
    public $propertyFromSecond;
}

class ThirdPropertyDeclarationClass extends SecondPropertyDeclarationClass
{
    public $propertyFromThird;
}
