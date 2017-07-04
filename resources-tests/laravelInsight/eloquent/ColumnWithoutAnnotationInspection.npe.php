<?php

use Illuminate\Database\Eloquent\Model;

class BaseModel extends Model {
    protected $timestamps = true;
    protected $primaryKey = 'id';
}

class ShouldBeRemovedProgramatically extends BaseModel {
}

trait CCNullTargetReference {
    use CCWillBeRemoved;
}
