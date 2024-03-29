# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project does not adhere to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0.8] - 2023-04-25

### Fixed
- Fixed change in packet ids

### Added
- Added feature to export names sniffed into a /who image
- Added Moonlight Village sprite images
- Added Shatters normal run requirement sheet

## [1.0.0.7] - 2023-03-17

### Fixed
- Fix error upon creating a private Realmeye character.

## [1.0.0.6] - 2023-03-17

### Fixed
- Items such as Sulfuric Stone not displaying properly due to Deca.
- Class data updates dynamically for future class stat changes.
- Stats that are not maxed now show properly when copied in the table.

## [1.0.0.5] - 2023-02-14
### Added
- Multiple requirement sheets to be used with the sniffer to parse.
- Parse Veteran sheets that use a point system.
- Ability to add custom points for each class to a requirement sheet.
- Parse maxed stats of players sniffed based on the requirement sheet.
- Copying the inventory image now displays custom messages.
- Added custom item ids for items that aren't stored by DECA.
- Added a guild parse with the WebApp.

### Changed
- You can no longer view account information through a button on the SetTable.

### Fixed
- Changed Fungal to Steamworks in Exalt Calculator

## [1.0.0.4] - 2022-11-16
### Added
- Added a new color to the list for displaying alt accounts.
- Added images for new ST sets.
- Checks Realmeye on launch to alert servers that cannot be accessed.
- Checks Github on launch to alert if there is a new version of OsancTools.
- New item images.

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
