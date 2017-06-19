<?php

use Illuminate\Database\Eloquent\Model;

class NotAEloquentModel
{
    public function notAMethodOfEloquentModel()
    {
    }
}

$notAEloquentInstance = new NotAEloquentModel;
$notAEloquentInstance->not_a_property_of_a_eloquent_model;

class <weak_warning descr="Column was not annotated as @property $id">EloquentModel</weak_warning> extends Model
{
    public $not_a_column_because_is_public;
}

$eloquentInstance = new EloquentModel;
$eloquentInstance->not_a_column_because_is_public;

class IndepententType
{
}

$independentType = new IndepententType;
$independentType->{'complex_name_not_supported'};

(<error descr="Expected: expression">)</error>->is_a_error_but_shoud_be_analyzed;

class <weak_warning descr="Column was not annotated as @property $id">EloquentModel_EmptyCasts</weak_warning> extends Model
{
    protected $casts = [];
}

class <weak_warning descr="Column was not annotated as @property $id">EloquentModel_DatesWithoutStringLiteral</weak_warning> extends Model
{
    protected $dates = [ 123 ];
}
