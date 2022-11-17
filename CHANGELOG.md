# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project does not adhere to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0.4] - 2022-11-16
### Added
- Added a new color to the list for displaying alt accounts.
- Added images for new ST sets.
- Checks Realmeye on launch to alert servers that cannot be accessed.
- Checks Github on launch to alert if there is a new version of OsancTools.

### Changed
- Fixed the slow react not displaying properly (https://github.com/Waifu/OsancTools/issues/7)
- Fixed parsed inventory carrying over to other reacts. (https://github.com/Waifu/OsancTools/issues/8)
- Limit description of the raid so that the application doesn't stretch.
- Character metadata is now stored properly (Level/Last Seen/etc).

## [1.0.0.3] - 2022-11-05
### Changed
- Fixed slow items from not being parsed properly

## [1.0.0.2] - 2022-10-25
### Added
- Set/react tables now color code raiders with multiple accounts.
- Checking one of the accounts checks all that are visible.

### Changed
- VC Parse now has a better layout
- Raiders with celestial are now indicated by a yellow font color.
- Copying the image in the cell copies the discord id of the raider.
- Metadata such as character level, CQC, fame, etc is not being logged due to Realmeye issues. Will be fixed in a new version.

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
