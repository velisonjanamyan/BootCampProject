Levon, [31.01.25 17:09]
# Item Upgrade System

A Java application demonstrating an item inventory with upgrade mechanics, all organized into well-separated components. You can create items of various rarities, store them in a singleton Inventory, and upgrade them according to specific rules. This README explains the project’s design, features, and how to run it.

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Design & Architecture](#design--architecture)
   - [Singleton Pattern](#singleton-pattern)
   - [Data Structure (Nested Maps)](#data-structure-nested-maps)
   - [Upgrade Logic (Service Layer)](#upgrade-logic-service-layer)
   - [CLI Interface (Main Class)](#cli-interface-main-class)
4. [Prerequisites](#prerequisites)
5. [Installation and Setup](#installation-and-setup)
6. [Usage](#usage)
7. [Project Structure](#project-structure)
8. [Notes on Serialization](#notes-on-serialization)
9. [Contributing](#contributing)
10. [License](#license)

---

## Overview

This application showcases:
- A Singleton Inventory that stores items using a nested map structure.
- An UpgradeService that enforces different upgrade rules for common/great/rare items vs. epic items (which have levels 0, 1, 2).
- A simple command-line interface in Main to create items, list the inventory, upgrade items, and save/load data from a file.

---

## Features

1. Create Items  
   - Specify an item name and rarity (e.g. COMMON, RARE), automatically stored at level 0 (except EPIC levels are handled specially).
   - Prevents creation of a new rarity for an existing item name unless it’s done via the upgrade path.

2. Display Inventory  
   - Shows items grouped by name → rarity → level, along with how many copies you have.

3. Upgrade Items  
   - Non-EPIC rarities (e.g., COMMON, GREAT, RARE) typically require 3 copies to upgrade to the next rarity.  
   - EPIC rarities have sub-levels:
     - EPIC(0) → EPIC(1) needs 2 items at EPIC(0).  
     - EPIC(1) → EPIC(2) may require items from EPIC(0) and EPIC(1).  
     - EPIC(2) → LEGENDARY requires 3 items at EPIC(2).  

4. Save/Load Inventory  
   - Persists your entire inventory in a binary file (.ser).  
   - Restores it later, replacing the in-memory data.

---

## Design & Architecture

### Singleton Pattern

- The Inventory class uses a singleton design.  
  - A private constructor prevents direct instantiation.  
  - A static `getInstance()` method returns the single Inventory object.  

Why Singleton?  
We want a single authoritative source of items across the application. Once you create or upgrade items, the entire program sees the same data.

### Data Structure (Nested Maps)

Each item is stored in a:
```java
Map<String, Map<Rarity, Map<Integer, Integer>>>
Key: String item name (e.g., "SWORD").
Value: Another map (by Rarity).
Which in turn maps levels to counts (Integer → Integer).
Why Nested Maps?

We often need to handle multiple rarities for the same item name, and multiple levels (especially for EPIC) with different counts.
It’s easy to increment or decrement counts when creating or removing items.
Upgrade Logic (Service Layer)
The UpgradeService class:

Separates upgrade rules from the storage logic.
Defines how many items are required to move from one rarity/level to another.
Relies on Inventory to check item counts, remove the required items, and add the new upgraded item.
Key Benefit:
Having a separate service class keeps Inventory simpler (it just stores data) and places game logic (upgrade requirements) in a dedicated module.

CLI Interface (Main Class)
Main provides a text-based menu:
Create Item
Display Inventory
Upgrade Item
Save Inventory
Load Inventory
Exit
Uses Scanner to collect user input, then calls methods in Inventory or UpgradeService.
Why a Simple CLI?
Demonstrates how someone might interact with this system in a straightforward way. In a larger project, the same logic could be connected to a GUI or a web service.

Levon, [31.01.25 17:09]
Prerequisites
Java 8+ (or higher)
A command-line terminal or an IDE (e.g., Eclipse, IntelliJ, VSCode) to run the application.
Installation and Setup
Clone or Download this repository to your local machine.
Check you have Java 8+ installed (java -version).
Navigate to the project root directory.
Compile via Command Line
bash
Copy
Edit
javac main/Main.java
This compiles:

Inventory.java in inventory package
Rarity.java in items package
UpgradeService.java in upgrade package
Main.java in main package
Run
bash
Copy
Edit
java main.Main
You should see a menu prompting you for your choice (1-6).

Usage
Main Menu:

markdown
Copy
Edit
Select an option:
1. Create Item
2. Display Inventory
3. Upgrade Item
4. Save Inventory
5. Load Inventory
6. Exit
Your choice:
Create Item

Enter an item name (e.g., "Sword") and a rarity (e.g., "COMMON").
If valid, one copy of the item is added at level 0 (or whichever level is appropriate).
Display Inventory

Prints a tree-like view: Item: SWORD, Rarity: COMMON, Count: 3, etc.
For EPIC rarities, it shows the level (0, 1, or 2) and the count at that level.
Upgrade Item

Enter the existing item name and rarity (and level if EPIC).
If there are enough copies, it removes them and adds one copy of the upgraded rarity/level.
Save Inventory

Provide a filename (e.g., inventory.ser).
The entire Inventory is written out via Java’s ObjectOutputStream.
Load Inventory

Specify the .ser file to read.
The in-memory inventory is replaced with whatever data was saved earlier.
Exit

Ends the program.
Project Structure
less
Copy
Edit
.
├── inventory
│   └── Inventory.java       // Singleton class, manages item data, create/remove methods
├── items
│   └── Rarity.java          // Enum: COMMON, GREAT, RARE, EPIC, LEGENDARY
├── upgrade
│   └── UpgradeService.java  // Upgrade logic: checks counts, removes items, adds upgraded version
└── main
    └── Main.java            // CLI entry point
Notes on Serialization
Inventory is a singleton. If you have a readResolve() that returns getInstance(), you might end up discarding the newly loaded data.
By default, we assign the deserialized object to Inventory.instance so that the loaded data takes effect.
Static fields are not automatically serialized. Only instance fields are stored when writing out the object.
Contributing
Fork this repo on GitHub.
Create a branch (e.g. feature/extra-upgrade-rules).
Commit your changes and push to your fork.
Open a Pull Request with a clear description of your changes.
