# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## 0.3.0 [Unreleased]

### Added

* **Fluent:** property without annotation inspection;
* **Fluent**: should not be instantiated directly;
* **Fluent**: should not be used as type directly;
* **Blade**: support to folding Blade statements and comments;
* **Resolution**: improvement to variable types resolution (eg. when inside of parentheses);

## 0.2.0 [[Digon Release](https://github.com/rentalhost/laravel-insight/releases/tag/0.2.0)] - 2017-06-09

### Added

* **Plugin**: is now distributed on official Jetbrains plugins repository ([see](https://plugins.jetbrains.com/plugin/9730-laravel-insight));
* [#10] **Query Scopes**: *ctrl+click* on a scoped method resolve to scope declaration ([more info](https://github.com/rentalhost/laravel-insight/wiki/Auxiliary-Features#query-scopes));
* [#10] **Query Scopes**: code completion to scope methods ([more info](https://github.com/rentalhost/laravel-insight/wiki/Auxiliary-Features#query-scopes));
* [#8] **Inspection**: *column without annotation* now try to guess the column type based on some contexts ([more info](https://github.com/rentalhost/laravel-insight/wiki/Inspections#column-without-annotation));

### Fixed

* **NPE**: when class name identifier is absent (eg. `class {}`);

## 0.1.1 - 2017-06-01

### Changed

* **Eloquent**: *property without annotation* renamed to *column without annotation*;

### Fixed

* **NPE**: on skip parenteshes, consider that it could have any argument yet (eg. `()->id`);

## 0.1.0 [[Monogon Release](https://github.com/rentalhost/laravel-insight/releases/tag/0.1.0)] - 2017-05-31

### Added

* Initial development release;
* **Eloquent:** column without annotation inspection ([more info](https://github.com/rentalhost/laravel-insight/wiki/Inspections#column-without-annotation));

