<?php

namespace Illuminate\Support {
    class Fluent
    {
    }
}

namespace {
    use Illuminate\Support\Fluent;

    /**
     * @property <weak_warning descr="Fluent should not be used as type.">Fluent</weak_warning> $fluent
     */
    class MyFluent
    {
        /** @var <weak_warning descr="Fluent should not be used as type.">Fluent</weak_warning> */
        public $x;

        public static function get(<weak_warning descr="Fluent should not be used as type.">Fluent $type</weak_warning>):
            <weak_warning descr="Fluent should not be used as type.">Fluent</weak_warning>
        {
        }
    }

    function test(): <weak_warning descr="Fluent should not be used as type.">Fluent</weak_warning>
    {
    }
}
