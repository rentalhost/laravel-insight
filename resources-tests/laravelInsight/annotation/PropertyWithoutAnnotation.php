<?php

use Illuminate\Database\Eloquent\Model;

class EloquentCasts_WithoutPhpdoc extends Model
{
    protected $casts = [
        <weak_warning descr="@property $someProperty was not annotated">'someProperty'</weak_warning> => 'int'
    ];
}

/** Class */
class EloquentCasts_WithPhpdoc_WithoutParam extends Model
{
    protected $casts = [
        <weak_warning descr="@property $someProperty was not annotated">'someProperty'</weak_warning> => 'int'
    ];
}

/**
 * @property $anotherProperty
 */
class EloquentCasts_WithPhpdoc_WithParam_WithoutSomePropertyParam extends Model
{
    protected $casts = [
        <weak_warning descr="@property $someProperty was not annotated">'someProperty'</weak_warning> => 'int'
    ];
}

/**
 * @property $someProperty
 */
class EloquentCasts_WithPhpdoc_WithParam_WithSomePropertyParam extends Model
{
    protected $casts = [
        'someProperty' => 'int'
    ];
}

class EloquentCasts_WithInvalidType extends Model
{
    protected $casts = 123;
}

class EloquentCasts_WithInvalidHashKeys extends Model
{
    protected $casts = [
        123 => 'int',
        'string'
    ];
}

class EloquentCasts_ShouldIgnoresNonStringValues extends Model
{
    protected $casts = [
        'shouldBeIgnored' => 123
    ];
}

class EloquentCasts_WithoutCastsField extends Model
{
}

class CC_EloquentCasts_WithOtherFields extends Model
{
    protected $CC_otherField;
}

class NoEloquentModel
{
}

class CC_NoEloquentModel_WithCastsField
{
    protected $casts = [ 'CC' ];
}
