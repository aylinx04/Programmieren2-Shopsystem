# Programmieren 2 - Shopsystem

## Übersicht

In diesem Modul, wurde in Gruppenarbeit ein Shopsystem realisiert mit Java. In diesem Projekt haben wir eine saubere Architektur, mit Exceptions, Objektklassen, mit einer CUI und GUI gearbeitet.

## Architektur

```bash
├───ModuleClient
│   └───src
│       └───client
│           ├───net
│           └───ui
│               ├───cui
│               └───gui
├───ModuleCommon
│   └───src
│       └───common
│           └───exceptions
├───ModuleServer
│   └───src
│       └───server
│           ├───domain
│           ├───net
│           └───persistence
```
Wie man in dieser Baumansicht sieht, wurde das Projekt in drei größeren Module aufgeteilt. Dies dient zu einer verständnisvollen Übersicht.

## Objektklassen

```bash
│   Artikel.java
│   Ereignis.java
│   Kunde.java
│   Massengutartikel.java
│   Mitarbeiter.java
│   Rechnung.java
│   Warenkorb.java
```
Diese Objektklassen haben ihre eigenen Attributen und manche erben voneinander.
## Exceptions


```bash
│   ArtikelExistiertBereitsException.java
│   ArtikelNichtGefundenException.java
│   BestandNichtVorhandenException.java
│   LoginFehlgeschlagenException.java
│   MitarbeiterExistiertBereitsException.java
│   PackungsgroesseException.java
│   RegistrierenFehlgeschlagenException.java
```

## Persistence


```bash
│   Shop_A.txt
│   Shop_E.txt
│   Shop_K.txt
│   Shop_M.txt
```
Die Persistence Speicherung folgte in diesen ```.txt``` - Dateien.

## Starten der CUI

Hierfür muss erst die ```ShopServer.java``` gestartet werden und dann die ```CUI.java```. Folgendes sollte angezeigt werden:

```bash
Befehle:                      
Registrieren:                '0'
Einloggen:                   '1'
Artikelliste anzeigen:       '2'
--------------------------------
Beenden:                     'q'
```
## Starten der GUI

Hierfür muss erst die ```ShopServer.java``` gestartet werden und dann die ```GUI.java```. Folgendes sollte in der Konsole angezeigt werden:

<img width="855" height="592" alt="Screenshot 2026-05-16 151622" src="https://github.com/user-attachments/assets/bcf46df5-e939-40b6-88c9-dfdb72beb5a7" />

## Benotung

Für dieses Projekt wurde die Note 1,0 erreicht.


