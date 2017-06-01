<?php

use Illuminate\Database\Eloquent\Model;

class <weak_warning descr="@property $id was not annotated">EloquentSimulation</weak_warning> extends Model
{
    protected $keyType = 'int';
}

class <weak_warning descr="@property $id was not annotated">EloquentSimulationAlternative</weak_warning> extends Model
{
    protected $keyType = 'string';
}

class <weak_warning descr="@property $id was not annotated">Eloquent_IdInt</weak_warning> extends EloquentSimulation
{
}

class <weak_warning descr="@property $id was not annotated">Eloquent_IdString</weak_warning> extends EloquentSimulation
{
}

(new Eloquent_IdInt)-><weak_warning descr="@property $id was not annotated">id</weak_warning>;
(new Eloquent_IdString)-><weak_warning descr="@property $id was not annotated">id</weak_warning>;

class <weak_warning descr="@property $id was not annotated">Eloquent_IdSuffix</weak_warning> extends EloquentSimulation
{
}

(new Eloquent_IdSuffix)-><weak_warning descr="@property $user_id was not annotated">user_id</weak_warning>;

class <weak_warning descr="@property $id was not annotated">Eloquent_IdentifyAccessorReturnType</weak_warning> extends EloquentSimulation
{
    public function <weak_warning descr="@property $identify_as_string was not annotated">getIdentifyAsStringAttribute</weak_warning>(): string {
    }
}

(new Eloquent_IdentifyAccessorReturnType)-><weak_warning descr="@property $identify_as_string was not annotated">identify_as_string</weak_warning>;

class <weak_warning descr="@property $id was not annotated">Eloquent_IdentifyMutatorReturnType_NoAccessor</weak_warning> extends EloquentSimulation
{
}

(new Eloquent_IdentifyMutatorReturnType_NoAccessor)-><weak_warning descr="@property $identify_as_mixed was not annotated">identify_as_mixed</weak_warning>;

class <weak_warning descr="@property $id was not annotated">Eloquent_IdentifyMutatorReturnType_GetFromAcessor</weak_warning> extends EloquentSimulation
{
    public function <weak_warning descr="@property $identify_as_int was not annotated">getIdentifyAsIntAttribute</weak_warning>(): int {
    }
}

(new Eloquent_IdentifyMutatorReturnType_GetFromAcessor)-><weak_warning descr="@property $identify_as_int was not annotated">identify_as_int</weak_warning>;

class <weak_warning descr="@property $id was not annotated">Eloquent_IdentifyCastType</weak_warning> extends EloquentSimulation
{
    protected $casts = [
        <weak_warning descr="@property $property_int was not annotated">'property_int'</weak_warning> => 'int',
        <weak_warning descr="@property $property_integer was not annotated">'property_integer'</weak_warning> => 'integer',
        <weak_warning descr="@property $property_real was not annotated">'property_real'</weak_warning> => 'real',
        <weak_warning descr="@property $property_float was not annotated">'property_float'</weak_warning> => 'float',
        <weak_warning descr="@property $property_double was not annotated">'property_double'</weak_warning> => 'double',
        <weak_warning descr="@property $property_string was not annotated">'property_string'</weak_warning> => 'string',
        <weak_warning descr="@property $property_bool was not annotated">'property_bool'</weak_warning> => 'bool',
        <weak_warning descr="@property $property_boolean was not annotated">'property_boolean'</weak_warning> => 'boolean',
        <weak_warning descr="@property $property_object was not annotated">'property_object'</weak_warning> => 'object',
        <weak_warning descr="@property $property_array was not annotated">'property_array'</weak_warning> => 'array',
        <weak_warning descr="@property $property_json was not annotated">'property_json'</weak_warning> => 'json',
        <weak_warning descr="@property $property_collection was not annotated">'property_collection'</weak_warning> => 'collection',
        <weak_warning descr="@property $property_date was not annotated">'property_date'</weak_warning> => 'date',
        <weak_warning descr="@property $property_datetime was not annotated">'property_datetime'</weak_warning> => 'datetime',
        <weak_warning descr="@property $property_timestamp was not annotated">'property_timestamp'</weak_warning> => 'timestamp',
        <weak_warning descr="@property $property_mixed was not annotated">'property_mixed'</weak_warning> => 'anything_else',
    ];
}
