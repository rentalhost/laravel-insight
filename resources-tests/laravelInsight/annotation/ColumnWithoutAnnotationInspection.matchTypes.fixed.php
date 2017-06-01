<?php

use Illuminate\Database\Eloquent\Model;

/**
 * @property int $id
 */
class EloquentSimulation extends Model
{
    protected $keyType = 'int';
}

/**
 * @property string $id
 */
class EloquentSimulationAlternative extends Model
{
    protected $keyType = 'string';
}

/**
 * @property int $id
 */
class Eloquent_IdInt extends EloquentSimulation
{
}

/**
 * @property int $id
 */
class Eloquent_IdString extends EloquentSimulation
{
}

(new Eloquent_IdInt)->id;
(new Eloquent_IdString)->id;

/**
 * @property int $id
 * @property int $user_id
 */
class Eloquent_IdSuffix extends EloquentSimulation
{
}

(new Eloquent_IdSuffix)->user_id;

/**
 * @property int $id
 * @property string $identify_as_string
 */
class Eloquent_IdentifyAccessorReturnType extends EloquentSimulation
{
    public function getIdentifyAsStringAttribute(): string {
    }
}

(new Eloquent_IdentifyAccessorReturnType)->identify_as_string;

/**
 * @property int $id
 * @property mixed $identify_as_mixed
 */
class Eloquent_IdentifyMutatorReturnType_NoAccessor extends EloquentSimulation
{
}

(new Eloquent_IdentifyMutatorReturnType_NoAccessor)->identify_as_mixed;

/**
 * @property int $id
 * @property int $identify_as_int
 */
class Eloquent_IdentifyMutatorReturnType_GetFromAcessor extends EloquentSimulation
{
    public function getIdentifyAsIntAttribute(): int {
    }
}

(new Eloquent_IdentifyMutatorReturnType_GetFromAcessor)->identify_as_int;

/**
 * @property int $id
 * @property int $property_int
 * @property int $property_integer
 * @property float $property_real
 * @property float $property_float
 * @property float $property_double
 * @property string $property_string
 * @property bool $property_bool
 * @property bool $property_boolean
 * @property object $property_object
 * @property array $property_array
 * @property array $property_json
 * @property \Illuminate\Support\Collection $property_collection
 * @property \Carbon\Carbon $property_date
 * @property \Carbon\Carbon $property_datetime
 * @property \Carbon\Carbon $property_timestamp
 * @property mixed $property_mixed
 */
class Eloquent_IdentifyCastType extends EloquentSimulation
{
    protected $casts = [
        'property_int' => 'int',
        'property_integer' => 'integer',
        'property_real' => 'real',
        'property_float' => 'float',
        'property_double' => 'double',
        'property_string' => 'string',
        'property_bool' => 'bool',
        'property_boolean' => 'boolean',
        'property_object' => 'object',
        'property_array' => 'array',
        'property_json' => 'json',
        'property_collection' => 'collection',
        'property_date' => 'date',
        'property_datetime' => 'datetime',
        'property_timestamp' => 'timestamp',
        'property_mixed' => 'anything_else',
    ];
}
