
# Updater

By default, when the application is launched for the first time, the database is empty.
YAML resources are stored at a remote site. This makes it possible to update the database remotely.
A manisfest file in YAML indicates the version and the links to the different resources.
This file contains :
- **name** : database's name, useful as a folder name.
- **version** : if a version is higher, the update is started.
- **data** : URL to YAML resources.
- **figs** : URL to figures.

An example of a manifest can be found [here](http://lorian.corbel.emi.u-bordeaux.fr/jumathsji/manifest.yaml).
**Data** and **figs** are stored in zip formats. After downloading, they are stored in the external storage of the smartphone in the folder with the **name** of the database.

The entire update mechanism is managed by the **UpdateDBTask** class.
> :warning:  the manifest file is stored in the smartphone to maintain the version. It is not always removed after uninstallation and may block an update. This can be fixed by upgrading the remote version. stored in the smartphone to maintain the version. It is not always removed after uninstallation and may block an update. This can be fixed by upgrading the remote version.