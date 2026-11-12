PvP Visuals — Fabric 1.21.11

Co robi mod:
- wykrywa gracza, na którego wskazuje celownik,
- podświetla wskazanego gracza obrysem Minecraft (glowing),
- pokazuje panel z nickiem, aktualnym HP, paskiem życia oraz ikonami wyposażenia.

Wymagania:
- Minecraft Java 1.21.11
- Fabric Loader zgodny z wersją 1.21.11
- Fabric API 0.141.6+1.21.11
- Java 21 lub nowsza

Budowanie pliku JAR:
1. Rozpakuj ZIP z projektem.
2. Otwórz folder projektu w IntelliJ IDEA jako projekt Gradle albo użyj terminala w tym folderze.
3. Uruchom: gradlew.bat build
4. Po udanym budowaniu plik moda będzie w build\libs\pvp-visuals-1.0.0.jar
5. Skopiuj ten plik do .minecraft\mods.

Uwaga: projekt jest kodem źródłowym moda, a nie gotowym plikiem JAR. Pierwsze budowanie pobiera zależności Gradle i Fabric z internetu.
