# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project does not adhere to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]


## [1.0.0.1] - 2022-10-06
### Added
- The following items are now DPS:
    - Elemental Equilibrium UT
    - His Majesty's Eminence UT
    - Chrysalis of Eternity UT
    - T14 Spellblade
    - T14 Longbow
    - Hama Yumi
    - Shaman's Staff
    - T7 Scepter
- Added functional VC Parse.
- Check ST sets and slow react combinations.
- Multiple ways to choose image for VC Parse.
- Template no longer used locally.

### Changed
- React objects are now created instead of react metadata being stored in a list.
- Class Reacts now check to see if T6+ is a requirement before parsing.
- Item objects no longer store a parsed image.
- Fixed an error that would pop up when clicking cancel after inputting a raid id. 
- Reskin items for DPS and T6 abilities now count for its react.

## [1.0.0.0] - 2022-05-26
### Added
- Initial source code.

### Changed
- Added GUI Scaling for GUI.java.
