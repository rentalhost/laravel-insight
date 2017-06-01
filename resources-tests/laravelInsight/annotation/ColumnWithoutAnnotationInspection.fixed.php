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

/**
 * @property int $id
 * @property int $someProperty
 */
class EloquentCasts_WithoutPhpdoc extends Model
{
    protected $casts = [
        'someProperty' => 'int'
    ];
}

/** Class
 * @property int $id
 * @property int $someProperty
 */
class EloquentCasts_WithPhpdoc_WithoutParam extends Model
{
    protected $casts = [
        'someProperty' => 'int'
    ];
}

/**
 * @property mixed $anotherProperty
 * @property int $id
 * @property int $someProperty
 */
class EloquentCasts_WithPhpdoc_WithParam_WithoutSomePropertyParam extends Model
{
    protected $casts = [
        'someProperty' => 'int'
    ];
}

/**
 * @property mixed $someProperty
 * @property int $id
 */
class EloquentCasts_WithPhpdoc_WithParam_WithSomePropertyParam extends Model
{
    protected $casts = [
        'someProperty' => 'int'
    ];
}

/**
 * @property int $id
 */
class EloquentCasts_WithInvalidType extends Model
{
    protected $casts = 123;
}

/**
 * @property int $id
 */
class EloquentCasts_WithInvalidHashKeys extends Model
{
    protected $casts = [
        123 => 'int',
        'string'
    ];
}

/**
 * @property int $id
 */
class EloquentCasts_ShouldIgnoresNonStringValues extends Model
{
    protected $casts = [
        'shouldBeIgnored' => 123
    ];
}

/**
 * @property int $id
 */
class EloquentCasts_ShouldIgnoresMultidimensionalKeys extends Model
{
    protected $casts = [
        'multidimensionalArray' => [
            'shouldBeIgnored' => 'int'
        ]
    ];
}

/**
 * @property int $id
 */
class CC_EloquentCasts_CastsIsNotArray extends Model
{
    /** @type array */
    protected $casts = 123;
}

/**
 * @property int $id
 * @property mixed $from_new_model_instance
 */
class EloquentCasts_WithoutCastsField extends Model
{
}

/**
 * @property int $id
 */
class CC_EloquentCasts_WithOtherFields extends Model
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

/**
 * @property int $id
 * @property \Carbon\Carbon $created_at
 * @property \Carbon\Carbon $updated_at
 */
class EloquentDates_CreatedAndUpdatedAt extends Model
{
    protected $dates = [
        'created_at' => 'DateTime',
        'updated_at' => 'DateTime',
    ];
}

/**
 * @property int $id
 * @property \Carbon\Carbon $created_at
 * @property \Carbon\Carbon $updated_at
 */
class EloquentDates_CreatedAndUpdatedAt_WithConstants extends Model
{
    const CREATED_AT = 'created_at';
    const UPDATED_AT = 'updated_at';

    const CC_DATETIME = 'DateTime';

    protected $dates = [
        self::CREATED_AT => self::CC_DATETIME,
        self::UPDATED_AT => self::CC_DATETIME,
    ];
}

/**
 * @property mixed $created_at
 * @property int $id
 */
class EloquentBaseProperties extends Model
{
}

/**
 * @property int $id
 * @property \Carbon\Carbon $updated_at
 */
class EloquentChild_ShouldConsiderParentProperties extends EloquentBaseProperties
{
    protected $dates = [
        'created_at' => 'DateTime',
        'updated_at' => 'DateTime',
    ];
}

/**
 * @property int $id
 * @property \Carbon\Carbon $deleted_at
 */
class Eloquent_TraitSoftDeletes_DirectUsageOfSoftDeletesTrait_ShouldDeclareDeletedAtProperty extends Model
{
    use SoftDeletes,
        ShouldNotReportsThat;
}

/**
 * @property int $id
 * @property \Carbon\Carbon $deleted_at
 */
class Eloquent_TraitSoftDeletes_ShouldDeclareDeletedAtProperty extends Model
{
    use SoftDeletesWrapper,
        ShouldNotReportsThat;
}

/**
 * @property mixed $deleted_at
 * @property int $id
 */
class Eloquent_TraitSoftDeletes_ShouldConsiderDeclarationOnClass extends Model
{
    use SoftDeletesWrapper,
        ShouldNotReportsThat;
}

/**
 * @property int $id
 */
class Eloquent_ShouldCheckIfIsTheRightSoftDeletesTrait extends Model
{
    use NotAnySoftDeletesTrait;
}

/**
 * @property int $id
 * @property \Carbon\Carbon $created_at
 * @property \Carbon\Carbon $updated_at
 */
class EloquentTimestamps_SimulatingTheEloquentModelProperty extends Model
{
    protected $timestamps = true;
}

/**
 * @property \Carbon\Carbon $created_at
 * @property int $id
 * @property \Carbon\Carbon $updated_at
 */
class EloquentTimestamps_SimulatingTheEloquentModelProperty_Child
    extends EloquentTimestamps_SimulatingTheEloquentModelProperty
{
}

/**
 * @property int $id
 */
class EloquentPrimaryKey_SimulatingTheEloquentModelProperty extends Model
{
    protected $primaryKey = 'id';
}

/**
 * @property int $id
 */
class EloquentPrimaryKey_SimulatingTheEloquentModelProperty_Child
    extends EloquentPrimaryKey_SimulatingTheEloquentModelProperty
{
}

class CC_EloquentPrimaryKey_PrimaryKeyWithoutDefaultValue extends Model
{
    protected $primaryKey;
}

/**
 * @property int $id
 * @property mixed $has_one
 * @property mixed $has_many
 * @property mixed $has_many_through
 * @property mixed $morph_to
 * @property mixed $morph_one
 * @property mixed $morph_many
 * @property mixed $morph_to_many
 * @property mixed $belongs_to
 * @property mixed $belongs_to_many
 */
class EloquentRelationship_SimulatingTheEloquentModelMethods extends Model
{
    /** @return HasOne */
    public function hasOne()
    {
    }

    /** @return HasMany */
    public function hasMany()
    {
    }

    /** @return HasManyThrough */
    public function hasManyThrough()
    {
    }

    /** @return MorphTo */
    public function morphTo()
    {
    }

    /** @return MorphOne */
    public function morphOne()
    {
    }

    /** @return MorphMany */
    public function morphMany()
    {
    }

    /** @return MorphToMany */
    public function morphToMany()
    {
    }

    /** @return BelongsTo */
    public function belongsTo()
    {
    }

    /** @return BelongsToMany */
    public function belongsToMany()
    {
    }
}

/**
 * @property int $id
 * @property mixed $has_one_property
 * @property mixed $has_many_property
 * @property mixed $has_many_through_property
 * @property mixed $morph_to_property
 * @property mixed $morph_one_property
 * @property mixed $morph_many_property
 * @property mixed $morph_to_many_property
 * @property mixed $belongs_to_property
 * @property mixed $belongs_to_many_property
 * @property mixed $some_property
 */
class EloquentRelationship_HasOne extends EloquentRelationship_SimulatingTheEloquentModelMethods
{
    public function hasOneProperty()
    {
        return $this->hasOne();
    }

    public function hasManyProperty()
    {
        return $this->hasMany();
    }

    public function hasManyThroughProperty()
    {
        return $this->hasManyThrough();
    }

    public function morphToProperty()
    {
        return $this->morphTo();
    }

    public function morphOneProperty()
    {
        return $this->morphOne();
    }

    public function morphManyProperty()
    {
        return $this->morphMany();
    }

    public function morphToManyProperty()
    {
        return $this->morphToMany();
    }

    public function belongsToProperty()
    {
        return $this->belongsTo();
    }

    public function belongsToManyProperty()
    {
        return $this->belongsToMany();
    }

    public function getSomePropertyAttribute()
    {
    }

    public function setSomePropertyAttribute()
    {
    }
}

/**
 * @property int $id
 * @property mixed $some_property
 * @property mixed $repeated_property
 */
class EloquentModel_MagicGetterAndSetter extends Model
{
    public $shouldBeIgnored_isAPublicProperty;
    protected $should_be_accepted;
}

$someModel = new EloquentModel_MagicGetterAndSetter;
$someModel->some_property = 5;
echo $someModel->some_property;

$someModel->shouldBeIgnored_onlyLowerCaseIsAllowed = 5;
echo $someModel->shouldBeIgnored_onlyLowerCaseIsAllowed;

$someModel->shouldBeIgnored_isAPublicProperty = 5;
echo $someModel->shouldBeIgnored_isAPublicProperty;

$notAModel = new NotAModel;
$notAModel->should_be_ignored_is_not_from_a_eloquent_model;

$externalReference = ExternalReference::getModel();
$externalReference->from_external_reference;

(new EloquentCasts_WithoutCastsField)->from_new_model_instance;

EloquentCasts_WithoutCastsField::$this_should_not_be_accepted;

$shouldNotifyOnlyTheFirst = new EloquentModel_MagicGetterAndSetter;
$shouldNotifyOnlyTheFirst->repeated_property = 5;
$shouldNotifyOnlyTheFirst->repeated_property = 5;

/**
 * @property int $id
 */
class CC_EloquentModel_WithoutTimestamps_ShouldNotBeReportedForTimestampsColumns extends Model {
    protected $timestamps = false;
}

/**
 * @property int $id
 */
class CC_EloquentModel_WithANotConstantTimestampsValue extends Model {
    protected $timestamps = 123;
}

class CC_EloquentModel_WithANotStringPrimaryKey extends Model {
    protected $primaryKey = 123;
}

/**
 * @property int $id
 */
class EloquentModel_PrimaryKeyColumnFromConstant extends Model {
    const PRIMARY_KEY = 'id';
    protected $primaryKey = self::PRIMARY_KEY;
}

/**
 * @property int $id
 * @property \Carbon\Carbon $created_at
 * @property \Carbon\Carbon $updated_at
 */
class EloquentModel_TimestampColumnFromConstant extends Model {
    const TIMESTAMPS_TRUE = true;
    protected $timestamps = self::TIMESTAMPS_TRUE;
}

/**
 * @property int $id
 */
class CC_EloquentModel_TimestampsUndefined extends Model {
    protected $timestamps;
}

/**
 * @property-read mixed $property_read
 * @property-write mixed $property_write
 * @property int $id
 */
class EloquentModel_PropertyReadAndWriteShouldBeAllowed extends Model
{
    public function propertyWrite()
    {
        $propertyReadAndWrite->property_write;
    }
}

$propertyReadAndWrite = new EloquentModel_PropertyReadAndWriteShouldBeAllowed;
$propertyReadAndWrite->property_read;
$propertyReadAndWrite->property_read; // twice
