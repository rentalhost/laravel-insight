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
