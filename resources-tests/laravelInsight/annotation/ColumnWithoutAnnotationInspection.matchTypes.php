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

