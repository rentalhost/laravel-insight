# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## 0.3.2: 2017-08-24

### Added
* **Blade**: supporting added to following directives: `@php` and `@push`;

### Fixed

* **Blade**: disabling folding feature from this plugin for directives `@if`, `@elseif`, `@unless`, `@forelse`, `@can`, `@elsecan`, `@cannot`, `@elsecannot` and `@hassection`, once that it was implemented by [**WI-36875**](https://youtrack.jetbrains.com/issue/WI-36875) - it still will be foldable, but will be handled now by PhpStorm;

## 0.3.1: 2017-07-04

### Fixed

* **NPE**: when `use` trait is temporarily empty (`use <caret>;`);

## 0.3.0: [Trigon Release](https://github.com/rentalhost/laravel-insight/releases/tag/0.3.0) - 2017-06-22

### Added

* **Fluent:** property without annotation inspection ([more info](https://github.com/rentalhost/laravel-insight/wiki/Inspections#fluent-property-without-annotation));
* **Fluent**: should not be instantiated directly ([more info](https://github.com/rentalhost/laravel-insight/wiki/Inspections#fluent-should-not-be-instantiated-directly));
* **Fluent**: should not be used as type directly ([more info](https://github.com/rentalhost/laravel-insight/wiki/Inspections#fluent-should-not-be-used-as-type-directly));
* **Blade**: support to folding Blade statements and comments ([more info](https://github.com/rentalhost/laravel-insight/wiki/Auxiliary-Features#blade-folding-support));

### Improvement

* **Resolution**: improvement to variable types resolution (eg. when inside of parentheses);

## 0.2.0: [Digon Release](https://github.com/rentalhost/laravel-insight/releases/tag/0.2.0) - 2017-06-09

### Added

* **Plugin**: is now distributed on official Jetbrains plugins repository ([more info](https://plugins.jetbrains.com/plugin/9730-laravel-insight));
* **Query Scopes**: *ctrl+click* on a scoped method resolve to scope declaration ([more info](https://github.com/rentalhost/laravel-insight/wiki/Auxiliary-Features#eloquent-query-scopes-support));
* **Query Scopes**: code completion to scope methods ([more info](https://github.com/rentalhost/laravel-insight/wiki/Auxiliary-Features#eloquent-query-scopes-support));
* **Eloquent Inspection**: *column without annotation* now try to guess the column type based on some contexts ([more info](https://github.com/rentalhost/laravel-insight/wiki/Inspections#eloquent-column-without-annotation));

### Fixed

* **NPE**: when class name identifier is absent (eg. `class {}`);

## 0.1.1: 2017-06-01

### Changed

* **Eloquent Inspection**: *property without annotation* renamed to *column without annotation*;

### Fixed

* **NPE**: on skip parenteshes, consider that it could have any argument yet (eg. `()->id`);

## 0.1.0: [Monogon Release](https://github.com/rentalhost/laravel-insight/releases/tag/0.1.0) - 2017-05-31

### Added

* Initial development release;
* **Eloquent Inspection:** column without annotation inspection ([more info](https://github.com/rentalhost/laravel-insight/wiki/Inspections#eloquent-column-without-annotation));

