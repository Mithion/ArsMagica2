ArsMagica2
==========

Ars Magica 2 Repository

### Useful Links
* [Homepage](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1292222)
* [Unofficial Wiki](http://am2.wikia.com/wiki/Ars_Magica_2_Wiki)
* [How to Report a Bug Properly](http://pastebin.com/29r0Nhe0)

### Commit structure
Make sure to keep your commits limited to one issue.  If you try to do too much in a single commit, you will be asked to split it out.
The reason for this is that if there is an issue with a commit requiring a rollback, this can be done without undoing all the other work.

### Code Style Guidelines
If you are interested in contributing to the project, you must follow the coding style guidelines.
There is a settings file for IDEA [here] (https://github.com/Mithion/ArsMagica2/blob/master/.idea/codeStyleSettings.xml)
It is part of the repo, and will be downloaded automatically.

The general gist of it is:
* Conditional blocks and their opening braces go on the same line with no space between the closing bracket and the opening brace (1)
	* This is the same for classes/methods
* Else if / else blocks go on the same line as the closing brace for the previous conditional (2)
	* No spaces between the braces and else
* Single line if statements should be braceless (3)
* Casts should not have spaces between the type and the variable (4)
* Look at existing files for further examples and formatting - just try to match our style!  It makes it easier to read and consistent :)

(1) Example:
```
if (distance_to_entity < 2f){ 

}
```

(2) Example:
```
if (distance_to_entity < 2f){

}else{

}
```

(3) Example:
```
if (entitySenses.canSee(entity))
	this.target = entity;
```

(4) Example:
```
((EntityLivingBase)entity).posX = 5;
```


### License
This mod is open sourced under the Creative Commons 3.0 Attribution Non-Commercial License
https://creativecommons.org/licenses/by-nc/3.0/legalcode

Summary
https://creativecommons.org/licenses/by-nc/3.0/

You are free to:
* Share — copy and redistribute the material in any medium or format
* Adapt — remix, transform, and build upon the material
* The licensor cannot revoke these freedoms as long as you follow the license terms.

Under the following conditions:
* Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
* NonCommercial — You may not use the material for commercial purposes.
* No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.

The exceptions to this license are textures, majority of which are property of D3miurge, licensed to this mod for it's use.
More information cam be found here: http://www.minecraftforum.net/user/2252536-d3miurge/#am2

Spell icons used include Painterly Spell Packs 1 through 4, courtesy of J. W. Bjerk (eleazzaar) at opengameart.com.

Some icons used courtesy of Ails http://www.alteredgamer.com/rpg-maker/100053-vx-custom-icons

Sounds are courtesy of opengameart.com and freesound.org, and licenses are that of their respective owners.