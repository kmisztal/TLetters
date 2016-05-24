# TLetters

## Założenia

W folderze z danymi przechowujemy:

- Foldery czcionek<br/>
  Nazwa folderu taka jak nazwa czcionki. W folderze znajdują się podfoldery z osobnymi rozmiarami, np. folder o nazwie “64x64” w którym będziemy przechowywać obrazki o rozmiarze 64x64 w formacie png.
  Znak ma być tak przeskalowany, aby stykał się z krawędziami obrazka jak największą ilościa punktów np. Dla ‘I’ punktami styku będzie kilka na górnej i kilka na dolnej krawędzi, dla "B" cała lewa krawędź, połowicznie dolna i górna oraz kilka punktów na prawej krawędzi. W skrócie: generujemy obrazek, wpisujemy literę i wyznaczamy prostokąt który ją zawiera i taki prostokąt przeskalowujemy do zadanego rozmiaru.

  Nazwy plików z obrazkami: unicodowa reprezentacja znaku, który przechowują.
  Np. dla pliku przechowujacego obrazek z ‘A’ nazwa wygląda następująco: 0041.png

- Pliki Properties [http://www.mkyong.com/java/java-properties-file-examples]()<br/>
  Dla każdej czcionki będziemy potrzebować plik Properties który zawierał będzie wektory cech każdej z liter.
  Kluczami będzie unicodowa reprezentacja znaku, a wartością wektor cech tego znaku z danej czcionki.
  Kolejne współrzędne wektora cech oddzielone od siebie znakiem białym.
  Kazda współrzędna wektora jest liczbą typu double.
  Początkowy zestaw wspieranych znaków to:
  * a-z	
  * A-Z
  * ąęćłńóśźż
  * ĄĘĆŁŃÓŚŹŻ
  * 0-9
  
## Gradle

### Run Main class
```shell
gradle :run
```

### Run GUI
```
gradle gui:run
```

### Run tests
```shell
gradle test
```
Test reports path: build/reports/tests

### Generate tests coverage report
```shell
gradle check
```
Coverage reports path: build/reports/jacoco