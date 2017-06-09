<?php

use Illuminate\Database\Eloquent\Model;

class Example extends Model
{
    public function scopeTest1() { }
    public function scopeTest2($builder) { }
    public function scopeTest3($builder, $var, $var2 = null) { }
}

$reference1 = (new Example)->reference;
$reference2 = (new Example)->reference();

class IsNotAModel
{
    public function scopeShouldNotBeApplicable() { }
}

$reference3 = (new IsNotAModel)->reference;

$reference4 = (new UnresolvedClass)->reference;

$reference5 = ("not an instance")->reference;
