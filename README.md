##### Plugin Name: Region
##### Server Version: 1.19.2
##### Description: A plugin to create and manage regions, only whitelisted users can interact in the region
##### Allowed libraries: You can only use a library for creating GUIs, any other lib or dependency is not allowed.

Commands:
- /region - Opens the regions menu
- /region create <name> - Creates a region at the selected location
- /region delete <name> - Deletea region with name
- /region wand - Gives the user a stick with a custom name to select locations to create a region
- /region add <name> <username> - Whitelist a user to a region
- /region remove <name> <username> - Removes a user from the region whitelist
- /region whitelist <name> - Lists the users in the region whitelist
- /region <name> - Opens the region menu

Menus:
    Regions Menu: A menu with all the existing regions where you can click one to open the region menu
    Region Menu: A menu with the following options to manage the region:
        - Rename
        - Whitelist add
        - Whitelist remove
        - Redefine location

Permissions:
- region.menu
- region.add
- region.remove
- region.whitelist
- region.create
- region.bypass

Storage type: MySQL

Deadline: 14 days