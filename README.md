# M2EMavenAARPlugin <img src="https://github.com/user-attachments/assets/eea7abc2-47ec-44ac-8d8a-c88cbb56af4f" height="35"/>

This is the accompanying Eclipse M2E Plugin (M2E Connector) for my [Maven AAR Unpack Plugin](https://github.com/dbeaudoinfortin/MavenAARUnpackPlugin). The Maven AAR Unpack Plugin allows you to use Android Archive Library (AAR) files directly within your Maven Java project. Without this companion Eclipse plugin, you can still manually run Maven to compile your project, but Eclipse will always show build errors since it doesn't know how to add AAR dependencies to the project classpath.

> [!TIP]
> You can manually download this plugin from the [releases section](https://github.com/dbeaudoinfortin/M2EMavenAARPlugin/releases) but you may find it easier to [install it from a site](#how-to-install) in Eclipse.

## How to install

This plugin is published on [GitHub Pages](https://dbeaudoinfortin.github.io/M2EMavenAARPlugin/), which is deployed directly from the [/docs](https://github.com/dbeaudoinfortin/M2EMavenAARPlugin/tree/main/docs) directory of this GitHub repository. The easiest way to install the plugin is via the menu option `Help -> Install New Software...`, then clicking the `Add...` button, entering the site `https://dbeaudoinfortin.github.io/M2EMavenAARPlugin/` in the `Location` box and clicking the `Add` button to add a new software installation site.

<p align="center">
  <img src="https://github.com/user-attachments/assets/2568573e-5bc4-4d93-8583-ce21eae9b006" width="400" /><br/>
  <img src="https://github.com/user-attachments/assets/64e11a46-8131-462b-b8b1-ac916692b7cf" width="400" /><br/>
  <img src="https://github.com/user-attachments/assets/689abf55-4522-4193-8588-66d532bc4eb0" width="400" /><br/>
  <img src="https://github.com/user-attachments/assets/e786feef-3658-4f61-93c3-8a6018dcfebe" width="400" /><br/>
  <img src="https://github.com/user-attachments/assets/aead149a-e97d-4324-ab12-d131b5957239" width="400" /><br/>
  <img src="https://github.com/user-attachments/assets/fa1734c5-e737-4c40-b132-7d583a59a081" width="400" /><br/>
</p>

## How it works

The Eclipse plugin acts as an Eclipse build participant in order to dynamically invoke the [Maven AAR Unpack Plugin](https://github.com/dbeaudoinfortin/MavenAARUnpackPlugin) during the build process. The Maven plugin scans the project for AAR dependencies, automatically resolves them, and unpacks them into the project target directory. The `classes.jar` JAR files from the extracted AAR files are then automatically added to the project's Java build path (classpath). 

For more details, see the [Maven AAR Unpack Plugin](https://github.com/dbeaudoinfortin/MavenAARUnpackPlugin).

## Requirements

- Requires Java 8 or later.
- Requires Eclipse 2024-12 (version 4.34) or later

## Legal Stuff

Copyright (c) 2025 David Fortin

This software is provided by David Fortin under the MIT License, meaning you are free to use it however you want, as long as you include the original copyright notice (above) and license notice in any copy you make. You just can't hold me liable in case something goes wrong. License details can be read [here](https://github.com/dbeaudoinfortin/M2EMavenAARPlugin?tab=MIT-1-ov-file)
