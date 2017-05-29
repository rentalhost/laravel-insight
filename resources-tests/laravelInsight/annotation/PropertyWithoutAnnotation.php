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

class EloquentCasts_ShouldIgnoresMultidimensionalKeys extends Model
{
    protected $casts = [
        'multidimensionalArray' => [
            'shouldBeIgnored' => 'int'
        ]
    ];
}

class CC_EloquentCasts_CastsIsNotArray extends Model
{
    /** @type array */
    protected $casts = 123;
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

class EloquentDates_CreatedAndUpdatedAt extends Model
{
    protected $dates = [
        <weak_warning descr="@property $created_at was not annotated">'created_at'</weak_warning> => 'DateTime',
        <weak_warning descr="@property $updated_at was not annotated">'updated_at'</weak_warning> => 'DateTime',
    ];
}

class EloquentDates_CreatedAndUpdatedAt_WithConstants extends Model
{
    const CREATED_AT = 'created_at';
    const UPDATED_AT = 'updated_at';

    const CC_DATETIME = 'DateTime';

    protected $dates = [
        <weak_warning descr="@property $created_at was not annotated">self::CREATED_AT</weak_warning> => self::CC_DATETIME,
        <weak_warning descr="@property $updated_at was not annotated">self::UPDATED_AT</weak_warning> => self::CC_DATETIME,
    ];
}

/**
 * @property $created_at
 */
class EloquentBaseProperties extends Model
{
}

class EloquentChild_ShouldConsiderParentProperties extends EloquentBaseProperties
{
    protected $dates = [
        'created_at' => 'DateTime',
        <weak_warning descr="@property $updated_at was not annotated">'updated_at'</weak_warning> => 'DateTime',
    ];
}
