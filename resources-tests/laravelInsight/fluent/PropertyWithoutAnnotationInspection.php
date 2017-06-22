<?php

namespace {
    use Illuminate\Support\Fluent as BaseFluent;

    class FacadeFluent extends BaseFluent
    {
    }
}

namespace Illuminate\Support {
    class Fluent
    {
    }
}

namespace Facades\Illuminate\Support {
    class Fluent
    {
    }
}

namespace MyNamespace {
    use Illuminate\Support\Fluent as FluentDirect;

    // Not acceptable: will not works directly to Fluent.
    $fluentInstantiatedDirectly = new FluentDirect;
    $fluentInstantiatedDirectly->shouldNotAccept;

    class ChildFluent extends Fluent
    {
        public $publicIsOkay;
    }

    // Acceptable: Fluent is intantiated via ChildFluent.
    $fluentInstantiatedIndirectly = new ChildFluent();
    $fluentInstantiatedIndirectly-><weak_warning descr="Property was not annotated as @property $shouldAccept">shouldAccept</weak_warning>;
    $fluentInstantiatedIndirectly->publicIsOkay;

    // Acceptable: static property.
    ChildFluent::<weak_warning descr="Property was not annotated as @property $staticShouldBeAccepted">$staticShouldBeAccepted</weak_warning>;

    // Not acceptable: will not works directly to Fluent, even from Facade.
    $fluentInstantiatedViaFacade = new \FacadeFluent();
    $fluentInstantiatedViaFacade->shouldNotAccept;
    \FacadeFluent::$shouldNotAccept;

    // Not acceptable: will not works directly to Fluent, even from Facade (as from Laravel 5.4+).
    $fluentInstantiatedViaFacadeL54 = new \Facades\Illuminate\Support\Fluent();
    $fluentInstantiatedViaFacade->shouldNotAccept;
    \Facades\Illuminate\Support\Fluent::$shouldNotAccept;

    // Code-coverage:
    $fluentInstantiatedIndirectly->{'fieldNameIsEmpty'};
}
