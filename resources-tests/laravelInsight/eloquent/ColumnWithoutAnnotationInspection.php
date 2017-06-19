<?php

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Database\Eloquent\Relations\HasOne;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Database\Eloquent\Relations\HasManyThrough;
use Illuminate\Database\Eloquent\Relations\MorphTo;
use Illuminate\Database\Eloquent\Relations\MorphOne;
use Illuminate\Database\Eloquent\Relations\MorphMany;
use Illuminate\Database\Eloquent\Relations\MorphToMany;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

trait SoftDeletesWrapper
{
    use SoftDeletes;
}

trait NotAnySoftDeletesTrait
{
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_WithoutPhpdoc</weak_warning> extends Model
{
    protected $casts = [
        <weak_warning descr="Column was not annotated as @property $someProperty">'someProperty'</weak_warning> => 'int'
    ];
}

/** Class */
class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_WithPhpdoc_WithoutParam</weak_warning> extends Model
{
    protected $casts = [
        <weak_warning descr="Column was not annotated as @property $someProperty">'someProperty'</weak_warning> => 'int'
    ];
}

/**
 * @property mixed $anotherProperty
 */
class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_WithPhpdoc_WithParam_WithoutSomePropertyParam</weak_warning> extends Model
{
    protected $casts = [
        <weak_warning descr="Column was not annotated as @property $someProperty">'someProperty'</weak_warning> => 'int'
    ];
}

/**
 * @property mixed $someProperty
 */
class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_WithPhpdoc_WithParam_WithSomePropertyParam</weak_warning> extends Model
{
    protected $casts = [
        'someProperty' => 'int'
    ];
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_WithInvalidType</weak_warning> extends Model
{
    protected $casts = 123;
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_WithInvalidHashKeys</weak_warning> extends Model
{
    protected $casts = [
        123 => 'int',
        'string'
    ];
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_ShouldIgnoresNonStringValues</weak_warning> extends Model
{
    protected $casts = [
        'shouldBeIgnored' => 123
    ];
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_ShouldIgnoresMultidimensionalKeys</weak_warning> extends Model
{
    protected $casts = [
        'multidimensionalArray' => [
            'shouldBeIgnored' => 'int'
        ]
    ];
}

class <weak_warning descr="Column was not annotated as @property $id">CC_EloquentCasts_CastsIsNotArray</weak_warning> extends Model
{
    /** @type array */
    protected $casts = 123;
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_WithoutCastsField</weak_warning> extends Model
{
}

class <weak_warning descr="Column was not annotated as @property $id">CC_EloquentCasts_WithOtherFields</weak_warning> extends Model
{
    protected $CC_otherField;
}

class NoEloquentModel
{
}

class CC_NoEloquentModel_WithCastsField
{
    protected $casts = ['CC'];
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_CreatedAndUpdatedAt</weak_warning> extends Model
{
    protected $casts = [
        <weak_warning descr="Column was not annotated as @property $created_at">'created_at'</weak_warning> => 'DateTime',
        <weak_warning descr="Column was not annotated as @property $updated_at">'updated_at'</weak_warning> => 'DateTime',
    ];
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_CreatedAndUpdatedAt_WithConstants</weak_warning> extends Model
{
    const CREATED_AT = 'created_at';
    const UPDATED_AT = 'updated_at';

    const CC_DATETIME = 'DateTime';

    protected $casts = [
        <weak_warning descr="Column was not annotated as @property $created_at">self::CREATED_AT</weak_warning> => self::CC_DATETIME,
        <weak_warning descr="Column was not annotated as @property $updated_at">self::UPDATED_AT</weak_warning> => self::CC_DATETIME,
    ];
}

/**
 * @property mixed $created_at
 */
class <weak_warning descr="Column was not annotated as @property $id">EloquentBaseProperties</weak_warning> extends Model
{
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentChild_ShouldConsiderParentProperties</weak_warning> extends EloquentBaseProperties
{
    protected $casts = [
        'created_at' => 'DateTime',
        <weak_warning descr="Column was not annotated as @property $updated_at">'updated_at'</weak_warning> => 'DateTime',
    ];
}

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_TraitSoftDeletes_DirectUsageOfSoftDeletesTrait_ShouldDeclareDeletedAtProperty</weak_warning> extends Model
{
    use <weak_warning descr="Column was not annotated as @property $deleted_at">SoftDeletes</weak_warning>,
        ShouldNotReportsThat;
}

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_TraitSoftDeletes_ShouldDeclareDeletedAtProperty</weak_warning> extends Model
{
    use <weak_warning descr="Column was not annotated as @property $deleted_at">SoftDeletesWrapper</weak_warning>,
        ShouldNotReportsThat;
}

/**
 * @property mixed $deleted_at
 */
class <weak_warning descr="Column was not annotated as @property $id">Eloquent_TraitSoftDeletes_ShouldConsiderDeclarationOnClass</weak_warning> extends Model
{
    use SoftDeletesWrapper,
        ShouldNotReportsThat;
}

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_ShouldCheckIfIsTheRightSoftDeletesTrait</weak_warning> extends Model
{
    use NotAnySoftDeletesTrait;
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentTimestamps_SimulatingTheEloquentModelProperty</weak_warning> extends Model
{
    protected <weak_warning descr="Column was not annotated as @property $created_at"><weak_warning descr="Column was not annotated as @property $updated_at">$timestamps</weak_warning></weak_warning> = true;
}

class <weak_warning descr="Column was not annotated as @property $created_at"><weak_warning descr="Column was not annotated as @property $id"><weak_warning descr="Column was not annotated as @property $updated_at">EloquentTimestamps_SimulatingTheEloquentModelProperty_Child</weak_warning></weak_warning></weak_warning>
    extends EloquentTimestamps_SimulatingTheEloquentModelProperty
{
}

class EloquentPrimaryKey_SimulatingTheEloquentModelProperty extends Model
{
    protected <weak_warning descr="Column was not annotated as @property $id">$primaryKey</weak_warning> = 'id';
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentPrimaryKey_SimulatingTheEloquentModelProperty_Child</weak_warning>
    extends EloquentPrimaryKey_SimulatingTheEloquentModelProperty
{
}

class CC_EloquentPrimaryKey_PrimaryKeyWithoutDefaultValue extends Model
{
    protected $primaryKey;
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentRelationship_SimulatingTheEloquentModelMethods</weak_warning> extends Model
{
    /** @return HasOne */
    public function <weak_warning descr="Column was not annotated as @property $has_one">hasOne</weak_warning>()
    {
    }

    /** @return HasMany */
    public function <weak_warning descr="Column was not annotated as @property $has_many">hasMany</weak_warning>()
    {
    }

    /** @return HasManyThrough */
    public function <weak_warning descr="Column was not annotated as @property $has_many_through">hasManyThrough</weak_warning>()
    {
    }

    /** @return MorphTo */
    public function <weak_warning descr="Column was not annotated as @property $morph_to">morphTo</weak_warning>()
    {
    }

    /** @return MorphOne */
    public function <weak_warning descr="Column was not annotated as @property $morph_one">morphOne</weak_warning>()
    {
    }

    /** @return MorphMany */
    public function <weak_warning descr="Column was not annotated as @property $morph_many">morphMany</weak_warning>()
    {
    }

    /** @return MorphToMany */
    public function <weak_warning descr="Column was not annotated as @property $morph_to_many">morphToMany</weak_warning>()
    {
    }

    /** @return BelongsTo */
    public function <weak_warning descr="Column was not annotated as @property $belongs_to">belongsTo</weak_warning>()
    {
    }

    /** @return BelongsToMany */
    public function <weak_warning descr="Column was not annotated as @property $belongs_to_many">belongsToMany</weak_warning>()
    {
    }
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentRelationship_HasOne</weak_warning> extends EloquentRelationship_SimulatingTheEloquentModelMethods
{
    public function <weak_warning descr="Column was not annotated as @property $has_one_property">hasOneProperty</weak_warning>()
    {
        return $this->hasOne();
    }

    public function <weak_warning descr="Column was not annotated as @property $has_many_property">hasManyProperty</weak_warning>()
    {
        return $this->hasMany();
    }

    public function <weak_warning descr="Column was not annotated as @property $has_many_through_property">hasManyThroughProperty</weak_warning>()
    {
        return $this->hasManyThrough();
    }

    public function <weak_warning descr="Column was not annotated as @property $morph_to_property">morphToProperty</weak_warning>()
    {
        return $this->morphTo();
    }

    public function <weak_warning descr="Column was not annotated as @property $morph_one_property">morphOneProperty</weak_warning>()
    {
        return $this->morphOne();
    }

    public function <weak_warning descr="Column was not annotated as @property $morph_many_property">morphManyProperty</weak_warning>()
    {
        return $this->morphMany();
    }

    public function <weak_warning descr="Column was not annotated as @property $morph_to_many_property">morphToManyProperty</weak_warning>()
    {
        return $this->morphToMany();
    }

    public function <weak_warning descr="Column was not annotated as @property $belongs_to_property">belongsToProperty</weak_warning>()
    {
        return $this->belongsTo();
    }

    public function <weak_warning descr="Column was not annotated as @property $belongs_to_many_property">belongsToManyProperty</weak_warning>()
    {
        return $this->belongsToMany();
    }

    public function <weak_warning descr="Column was not annotated as @property $some_property">getSomePropertyAttribute</weak_warning>()
    {
    }

    public function <weak_warning descr="Column was not annotated as @property $some_property">setSomePropertyAttribute</weak_warning>()
    {
    }
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentModel_MagicGetterAndSetter</weak_warning> extends Model
{
    public $shouldBeIgnored_isAPublicProperty;
    protected $should_be_accepted;
}

$someModel = new EloquentModel_MagicGetterAndSetter;
$someModel-><weak_warning descr="Column was not annotated as @property $some_property">some_property</weak_warning> = 5;
echo $someModel-><weak_warning descr="Column was not annotated as @property $some_property">some_property</weak_warning>;

$someModel->shouldBeIgnored_onlyLowerCaseIsAllowed = 5;
echo $someModel->shouldBeIgnored_onlyLowerCaseIsAllowed;

$someModel->shouldBeIgnored_isAPublicProperty = 5;
echo $someModel->shouldBeIgnored_isAPublicProperty;

$notAModel = new NotAModel;
$notAModel->should_be_ignored_is_not_from_a_eloquent_model;

$externalReference = ExternalReference::getModel();
$externalReference-><weak_warning descr="Column was not annotated as @property $from_external_reference">from_external_reference</weak_warning>;

(new EloquentCasts_WithoutCastsField)-><weak_warning descr="Column was not annotated as @property $from_new_model_instance">from_new_model_instance</weak_warning>;

EloquentCasts_WithoutCastsField::$this_should_not_be_accepted;

$shouldNotifyOnlyTheFirst = new EloquentModel_MagicGetterAndSetter;
$shouldNotifyOnlyTheFirst-><weak_warning descr="Column was not annotated as @property $repeated_property">repeated_property</weak_warning> = 5;
$shouldNotifyOnlyTheFirst-><weak_warning descr="Column was not annotated as @property $repeated_property">repeated_property</weak_warning> = 5;

class <weak_warning descr="Column was not annotated as @property $id">CC_EloquentModel_WithoutTimestamps_ShouldNotBeReportedForTimestampsColumns</weak_warning> extends Model {
    protected $timestamps = false;
}

class <weak_warning descr="Column was not annotated as @property $id">CC_EloquentModel_WithANotConstantTimestampsValue</weak_warning> extends Model {
    protected $timestamps = 123;
}

class CC_EloquentModel_WithANotStringPrimaryKey extends Model {
    protected $primaryKey = 123;
}

class EloquentModel_PrimaryKeyColumnFromConstant extends Model {
    const PRIMARY_KEY = 'id';
    protected <weak_warning descr="Column was not annotated as @property $id">$primaryKey</weak_warning> = self::PRIMARY_KEY;
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentModel_TimestampColumnFromConstant</weak_warning> extends Model {
    const TIMESTAMPS_TRUE = true;
    protected <weak_warning descr="Column was not annotated as @property $created_at"><weak_warning descr="Column was not annotated as @property $updated_at">$timestamps</weak_warning></weak_warning> = self::TIMESTAMPS_TRUE;
}

class <weak_warning descr="Column was not annotated as @property $id">CC_EloquentModel_TimestampsUndefined</weak_warning> extends Model {
    protected $timestamps;
}

/**
 * @property-read mixed $property_read
 * @property-write mixed $property_write
 */
class <weak_warning descr="Column was not annotated as @property $id">EloquentModel_PropertyReadAndWriteShouldBeAllowed</weak_warning> extends Model
{
    public function propertyWrite()
    {
        $propertyReadAndWrite->property_write;
    }
}

$propertyReadAndWrite = new EloquentModel_PropertyReadAndWriteShouldBeAllowed;
$propertyReadAndWrite->property_read;
$propertyReadAndWrite->property_read; // twice

class <weak_warning descr="Column was not annotated as @property $id">EloquentCasts_ShouldIgnoresEmptyColumnName</weak_warning> extends Model
{
    protected $casts = [
        '' => 'int'
    ];
}
