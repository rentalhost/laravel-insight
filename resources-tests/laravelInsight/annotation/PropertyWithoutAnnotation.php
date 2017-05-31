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

class Eloquent_TraitSoftDeletes_DirectUsageOfSoftDeletesTrait_ShouldDeclareDeletedAtProperty extends Model
{
    use <weak_warning descr="@property $deleted_at was not annotated">SoftDeletes</weak_warning>,
        ShouldNotReportsThat;
}

trait SoftDeletesWrapper
{
    use SoftDeletes;
}

class Eloquent_TraitSoftDeletes_ShouldDeclareDeletedAtProperty extends Model
{
    use <weak_warning descr="@property $deleted_at was not annotated">SoftDeletesWrapper</weak_warning>,
        ShouldNotReportsThat;
}

/**
 * @property $deleted_at
 */
class Eloquent_TraitSoftDeletes_ShouldConsiderDeclarationOnClass extends Model
{
    use SoftDeletesWrapper,
        ShouldNotReportsThat;
}

trait NotAnySoftDeletesTrait
{
}

class Eloquent_ShouldCheckIfIsTheRightSoftDeletesTrait extends Model
{
    use NotAnySoftDeletesTrait;
}

class EloquentTimestamps_SimulatingTheEloquentModelProperty extends Model
{
    protected <weak_warning descr="@property $created_at was not annotated"><weak_warning descr="@property $updated_at was not annotated">$timestamps</weak_warning></weak_warning> = true;
}

class <weak_warning descr="@property $created_at was not annotated"><weak_warning descr="@property $updated_at was not annotated">EloquentTimestamps_SimulatingTheEloquentModelProperty_Child</weak_warning></weak_warning>
    extends EloquentTimestamps_SimulatingTheEloquentModelProperty
{
}

class EloquentPrimaryKey_SimulatingTheEloquentModelProperty extends Model
{
    protected <weak_warning descr="@property $id was not annotated">$primaryKey</weak_warning> = 'id';
}

class <weak_warning descr="@property $id was not annotated">EloquentPrimaryKey_SimulatingTheEloquentModelProperty_Child</weak_warning>
    extends EloquentPrimaryKey_SimulatingTheEloquentModelProperty
{
}

class CC_EloquentPrimaryKey_PrimaryKeyWithoutDefaultValue extends Model
{
    protected $primaryKey;
}

class EloquentRelationship_SimulatingTheEloquentModelMethods extends Model
{
    /** @return HasOne */
    public function <weak_warning descr="@property $has_one was not annotated">hasOne</weak_warning>() {
    }

    /** @return HasMany */
    public function <weak_warning descr="@property $has_many was not annotated">hasMany</weak_warning>() {
    }

    /** @return HasManyThrough */
    public function <weak_warning descr="@property $has_many_through was not annotated">hasManyThrough</weak_warning>() {
    }

    /** @return MorphTo */
    public function <weak_warning descr="@property $morph_to was not annotated">morphTo</weak_warning>() {
    }

    /** @return MorphOne */
    public function <weak_warning descr="@property $morph_one was not annotated">morphOne</weak_warning>() {
    }

    /** @return MorphMany */
    public function <weak_warning descr="@property $morph_many was not annotated">morphMany</weak_warning>() {
    }

    /** @return MorphToMany */
    public function <weak_warning descr="@property $morph_to_many was not annotated">morphToMany</weak_warning>() {
    }

    /** @return BelongsTo */
    public function <weak_warning descr="@property $belongs_to was not annotated">belongsTo</weak_warning>() {
    }

    /** @return BelongsToMany */
    public function <weak_warning descr="@property $belongs_to_many was not annotated">belongsToMany</weak_warning>() {
    }
}

class EloquentRelationship_HasOne extends EloquentRelationship_SimulatingTheEloquentModelMethods
{
    public function <weak_warning descr="@property $has_one_property was not annotated">hasOneProperty</weak_warning>() {
        return $this->hasOne();
    }

    public function <weak_warning descr="@property $has_many_property was not annotated">hasManyProperty</weak_warning>() {
        return $this->hasMany();
    }

    public function <weak_warning descr="@property $has_many_through_property was not annotated">hasManyThroughProperty</weak_warning>() {
        return $this->hasManyThrough();
    }

    public function <weak_warning descr="@property $morph_to_property was not annotated">morphToProperty</weak_warning>() {
        return $this->morphTo();
    }

    public function <weak_warning descr="@property $morph_one_property was not annotated">morphOneProperty</weak_warning>() {
        return $this->morphOne();
    }

    public function <weak_warning descr="@property $morph_many_property was not annotated">morphManyProperty</weak_warning>() {
        return $this->morphMany();
    }

    public function <weak_warning descr="@property $morph_to_many_property was not annotated">morphToManyProperty</weak_warning>() {
        return $this->morphToMany();
    }

    public function <weak_warning descr="@property $belongs_to_property was not annotated">belongsToProperty</weak_warning>() {
        return $this->belongsTo();
    }

    public function <weak_warning descr="@property $belongs_to_many_property was not annotated">belongsToManyProperty</weak_warning>() {
        return $this->belongsToMany();
    }

    public function <weak_warning descr="@property $some_property was not annotated">getSomePropertyAttribute</weak_warning>() {
    }

    public function <weak_warning descr="@property $some_property was not annotated">setSomePropertyAttribute</weak_warning>() {
    }
}

class EloquentModel_MagicGetterAndSetter extends Model {
    public $shouldBeIgnored_isAPublicProperty;
    protected $should_be_accepted;
}

$someModel = new EloquentModel_MagicGetterAndSetter;
$someModel-><weak_warning descr="@property $some_property was not annotated">some_property</weak_warning> = 5;
echo $someModel-><weak_warning descr="@property $some_property was not annotated">some_property</weak_warning>;

$someModel->shouldBeIgnored_onlyLowerCaseIsAllowed = 5;
echo $someModel->shouldBeIgnored_onlyLowerCaseIsAllowed;

$someModel->shouldBeIgnored_isAPublicProperty = 5;
echo $someModel->shouldBeIgnored_isAPublicProperty;

$notAModel = new NotAModel;
$notAModel->should_be_ignored_is_not_from_a_eloquent_model;

$externalReference = ExternalReference::getModel();
$externalReference-><weak_warning descr="@property $from_external_reference was not annotated">from_external_reference</weak_warning>;

(new EloquentCasts_WithoutCastsField)-><weak_warning descr="@property $from_new_model_instance was not annotated">from_new_model_instance</weak_warning>;

EloquentCasts_WithoutCastsField::$this_should_not_be_accepted;
