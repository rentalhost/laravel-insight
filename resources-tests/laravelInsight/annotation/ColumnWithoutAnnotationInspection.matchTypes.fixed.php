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

