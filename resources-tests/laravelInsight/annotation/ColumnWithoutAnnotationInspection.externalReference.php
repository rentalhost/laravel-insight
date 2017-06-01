<?php

class IndirectModelReference extends \Illuminate\Database\Eloquent\Model
{
}

class ExternalReference
{
    public static function getModel()
    {
        return new IndirectModelReference;
    }
}
