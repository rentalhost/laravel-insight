<?php

use Illuminate\Database\Eloquent\Model;

class Example extends Model
{
    public function scopeTestReference() { }
}

class NotAModel
{
    public function scopeTestReference() { }
}

// Acceptables:
$shouldBeResolvable = (new Example)->testReference();

// Not acceptables:
$ccSimpleReference = 123;
$ccIsNotAScopedMethod = (new Example)->testReference;
$ccUnresolvableClass = (new UnresolvableClass)->testReference();
$ccNotAModel = (new NotAModel)->testReference();
$ccIsNotAScopedMethodOnModel = (new Example)->notAScopedMethod();
