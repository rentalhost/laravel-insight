# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## 0.2.0 - [Unreleased]

### Added

* **Inspection**: *column without annotation* now try to guess the column type (read the [doc](https://github.com/rentalhost/laravel-insight/wiki/Inspections#column-without-annotation) to more information);

## 0.1.1 - 2017-06-01

### Changed

* **Inspection**: *property without annotation* renamed to *column without annotation*;

### Fixed

* **NPE**: on skip parenteshes, consider that it could have any argument yet (eg. `()->id`);

## 0.1.0 [[Monogon Release](https://github.com/rentalhost/laravel-insight/releases/tag/0.1.0)] - 2017-05-31

### Added

* Initial development release;
* **Inspection:** column without annotation ([doc](https://github.com/rentalhost/laravel-insight/wiki/Inspections#column-without-annotation));

