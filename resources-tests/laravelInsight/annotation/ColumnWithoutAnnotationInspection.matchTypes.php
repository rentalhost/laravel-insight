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
