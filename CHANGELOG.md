# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## 0.2.0 [Unreleased]

### Added

* [#10] **Query Scopes**: *ctrl+click* on a scoped method resolve to scope declaration ([more info](https://github.com/rentalhost/laravel-insight/wiki/Auxiliary-Features#query-scopes));
* [#10] **Query Scopes**: code completion to scope methods ([more info](https://github.com/rentalhost/laravel-insight/wiki/Auxiliary-Features#query-scopes));
* [#8] **Inspection**: *column without annotation* now try to guess the column type ([more info](https://github.com/rentalhost/laravel-insight/wiki/Inspections#column-without-annotation));

### Fixed

* **NPE**: when class name identifier is absent (eg. `class {}`);

## 0.1.1 - 2017-06-01

### Changed

* **Inspection**: *property without annotation* renamed to *column without annotation*;

### Fixed

* **NPE**: on skip parenteshes, consider that it could have any argument yet (eg. `()->id`);

## 0.1.0 [[Monogon Release](https://github.com/rentalhost/laravel-insight/releases/tag/0.1.0)] - 2017-05-31

### Added

* Initial development release;
* **Inspection:** column without annotation ([more info](https://github.com/rentalhost/laravel-insight/wiki/Inspections#column-without-annotation));

