# OCR

## Définition

La *Reconnaissance Optique de Caractères*(ROC), en anglais *Optical Character Recognition*(OCR), est technique qui, à partir d'un procédé optique, permet à un système informatique de lire et de stocker de façon automatique du texte dactylographié, imprimé ou manuscrit sans qu'on ait à retaper ce dernier.
Un logiciel OCR permet par exemple à partir d'un texte scanné, d'extraire la partie textuelle des images, et de l'éditer dans un logiciel de traitement de texte.

## Pourquoi ?

L'utilisation d'un OCR pour se projet est importante car sans OCR on ne pourrait pas différencier les différentes cartes du jeux.

## Avec quoi ?

Nous avons utilisé *Google Vision* car c'est l'OCR de Google qui est extrêmement performant et facile d'utilisation.

# Fonctionnement

Pour faire fonctionner le service de Google, on doit appeler ça librairie :
- `implementation 'com.google.android.gms:play-services-vision:20.1.3`

La classe `TextRecognitionActivity` permet d'ouvrir et de gérer l'autofocus.

La classe `TextAnalyzer` permet de recevoir et d'analyser les différents groupes de mots que l'OCR permet d'identifier.

La classe `SurfaceHolderCallback` permet de demander la permission de la caméra à l'utilisateur.

La classe `SearchTask` permet de faire correspondre les mots reçu par l'OCR avec la base de donnée contenant les cartes de jeux.