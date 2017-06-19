<?php

use Illuminate\Database\Eloquent\Model;

class <weak_warning descr="Column was not annotated as @property $id">EloquentSimulation</weak_warning> extends Model
{
    protected $keyType = 'int';
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentSimulationAlternative</weak_warning> extends Model
{
    protected $keyType = 'string';
}

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_IdInt</weak_warning> extends EloquentSimulation
{
}

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_IdString</weak_warning> extends EloquentSimulation
{
}

(new Eloquent_IdInt)-><weak_warning descr="Column was not annotated as @property $id">id</weak_warning>;
(new Eloquent_IdString)-><weak_warning descr="Column was not annotated as @property $id">id</weak_warning>;

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_IdSuffix</weak_warning> extends EloquentSimulation
{
}

(new Eloquent_IdSuffix)-><weak_warning descr="Column was not annotated as @property $user_id">user_id</weak_warning>;

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_IdentifyAccessorReturnType</weak_warning> extends EloquentSimulation
{
    public function <weak_warning descr="Column was not annotated as @property $identify_as_string">getIdentifyAsStringAttribute</weak_warning>(): string {
    }
}

(new Eloquent_IdentifyAccessorReturnType)-><weak_warning descr="Column was not annotated as @property $identify_as_string">identify_as_string</weak_warning>;

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_IdentifyMutatorReturnType_NoAccessor</weak_warning> extends EloquentSimulation
{
}

(new Eloquent_IdentifyMutatorReturnType_NoAccessor)-><weak_warning descr="Column was not annotated as @property $identify_as_mixed">identify_as_mixed</weak_warning>;

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_IdentifyMutatorReturnType_GetFromAcessor</weak_warning> extends EloquentSimulation
{
    public function <weak_warning descr="Column was not annotated as @property $identify_as_int">getIdentifyAsIntAttribute</weak_warning>(): int {
    }
}

(new Eloquent_IdentifyMutatorReturnType_GetFromAcessor)-><weak_warning descr="Column was not annotated as @property $identify_as_int">identify_as_int</weak_warning>;

class <weak_warning descr="Column was not annotated as @property $id">Eloquent_IdentifyCastType</weak_warning> extends EloquentSimulation
{
    protected $casts = [
        <weak_warning descr="Column was not annotated as @property $property_int">'property_int'</weak_warning> => 'int',
        <weak_warning descr="Column was not annotated as @property $property_integer">'property_integer'</weak_warning> => 'integer',
        <weak_warning descr="Column was not annotated as @property $property_real">'property_real'</weak_warning> => 'real',
        <weak_warning descr="Column was not annotated as @property $property_float">'property_float'</weak_warning> => 'float',
        <weak_warning descr="Column was not annotated as @property $property_double">'property_double'</weak_warning> => 'double',
        <weak_warning descr="Column was not annotated as @property $property_string">'property_string'</weak_warning> => 'string',
        <weak_warning descr="Column was not annotated as @property $property_bool">'property_bool'</weak_warning> => 'bool',
        <weak_warning descr="Column was not annotated as @property $property_boolean">'property_boolean'</weak_warning> => 'boolean',
        <weak_warning descr="Column was not annotated as @property $property_object">'property_object'</weak_warning> => 'object',
        <weak_warning descr="Column was not annotated as @property $property_array">'property_array'</weak_warning> => 'array',
        <weak_warning descr="Column was not annotated as @property $property_json">'property_json'</weak_warning> => 'json',
        <weak_warning descr="Column was not annotated as @property $property_collection">'property_collection'</weak_warning> => 'collection',
        <weak_warning descr="Column was not annotated as @property $property_date">'property_date'</weak_warning> => 'date',
        <weak_warning descr="Column was not annotated as @property $property_datetime">'property_datetime'</weak_warning> => 'datetime',
        <weak_warning descr="Column was not annotated as @property $property_timestamp">'property_timestamp'</weak_warning> => 'timestamp',
        <weak_warning descr="Column was not annotated as @property $property_mixed">'property_mixed'</weak_warning> => 'anything_else',
    ];
}

/**
 * @property \Carbon\Carbon $already_exists
 */
class <weak_warning descr="Column was not annotated as @property $id">Eloquent_MatchAnyDatesValueAsCarbon</weak_warning> extends EloquentSimulation
{
    const FROM_CONSTANT = 'from_constant';

    protected $dates = [
        'already_exists',
        <weak_warning descr="Column was not annotated as @property $my_date">'my_date'</weak_warning>,
        <weak_warning descr="Column was not annotated as @property $another_date">'another_date'</weak_warning>,
        <weak_warning descr="Column was not annotated as @property $from_constant">self::FROM_CONSTANT</weak_warning>,

        'multidimensional' => [
            'shouldBeIgnored'
        ]
    ];
}
