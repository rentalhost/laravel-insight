Testing Blade folding...

@if ($expr)<fold text=' {...} '>
    If folding with elseif and else.</fold>
@elseif ($expr)<fold text=' {...} '>
    Elseif folding.</fold>
@else<fold text=' {...} '>
    Else folding.
</fold>@endif

@yield ('will be ignored')

@if ($expr)<fold text=' {...} '>
    @yield ('will be ignored (code-coverage)')
</fold>@endif

@if ($expr)<fold text=' {...} '>
    If folding.
</fold>@endif

@if ($expr)<fold text=' {...} '>
    @if ($expr)<fold text=' {...} '>
        Nested folding.
    </fold>@endif
</fold>@endif

@if ($longEmptyContent)<fold text=' {...} '>

</fold>@endif

@if ($shouldNotBeFoldedBecauseHaveNoContent) @endif
@if ($shouldNotBeFoldedBecauseHaveNoContent) @elseif ($too) @else @endif
@if ($shouldNotBeFoldedBecauseHaveNoContent)@endif
@if($shouldNotBeFoldedBecauseHaveNoContent)@elseif($too)@else@endif

@unless ($expr)<fold text=' {...} '>
    Unless folding.</fold>
@else<fold text=' {...} '>
    Else folding.
</fold>@endunless

@isset ($expr)<fold text=' {...} '>
    Isset folding.
</fold>@endisset

@empty ($expr)<fold text=' {...} '>
    Empty folding.
</fold>@endempty

@for ($expr)<fold text=' {...} '>
    For folding.
</fold>@endfor

@foreach ($expr)<fold text=' {...} '>
    Foreach folding.
</fold>@endforeach

@forelse ($expr)<fold text=' {...} '>
    Foreach folding.</fold>
@empty<fold text=' {...} '>
    Empty folding.
</fold>@endforelse

@while ($expr)<fold text=' {...} '>
    While folding.
</fold>@endwhile

@section ($expr)<fold text=' {...} '>
    Section folding.
</fold>@endsection

@section ($expr)<fold text=' {...} '>
    Section folding.
</fold>@show

@section ($expr)<fold text=' {...} '>
    Section folding.
</fold>@overwrite

@section ($expr)<fold text=' {...} '>
    Section folding.
</fold>@stop

@section ($expr)<fold text=' {...} '>
    Section folding.
</fold>@append

@component ($expr)<fold text=' {...} '>
    Component folding.
</fold>@endcomponent

@slot ($expr)<fold text=' {...} '>
    Slot folding.
</fold>@endslot

@verbatim ($expr)<fold text=' {...} '>
    Verbatim folding.
</fold>@endverbatim

@can ($expr)<fold text=' {...} '>
    Can folding.</fold>
@elsecan ($expr)<fold text=' {...} '>
    ElseCan folding.</fold>
@else<fold text=' {...} '>
    Else folding.
</fold>@endcan

@cannot ($expr)<fold text=' {...} '>
    Cannot folding.</fold>
@elsecannot ($expr)<fold text=' {...} '>
    ElseCannot folding.</fold>
@else<fold text=' {...} '>
    Else folding.
</fold>@endcannot

Currently is not supported on PhpStorm (WI-36875):

@php
    $phpFolding;
@endphp

@push ($expr)
    Push folding.
@endpush

@hassection ($expr)
    HasSection folding.
@endif

@hassection ($expr)
    HasSection folding.
@else<fold text=' {...} '>
    ElseSection folding.
</fold>@endif

Check if unfinished @if is treated correctly

@if ($unfinishedFolding)
    @if ($itWasFinished)<fold text=' {...} '>
        It was finished.
</fold>@endif

{{-- Inline comments should not be folded. --}}
<fold text='{{-- Small comment. --}}'>{{--
    Small comment.
--}}</fold>
<fold text='{{-- Multiline comments could be f... --}}'>{{--
    Multiline comments could be folded.
--}}</fold>
